package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.chat.Chat;
import com.kakao.sunsuwedding.portfolio.PortfolioService;
import com.kakao.sunsuwedding.quotation.Quotation;
import com.kakao.sunsuwedding.quotation.QuotationJPARepository;
import com.kakao.sunsuwedding.quotation.QuotationStatus;
import com.kakao.sunsuwedding.review.ReviewResponse;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;
    private final CoupleJPARepository coupleJPARepository;

    private final PortfolioService portfolioService;

    @Transactional
    public void addMatch(Couple couple, Planner planner, Chat chat) {
        List<Match> matches = matchJPARepository.findByCoupleAndPlanner(couple, planner);
        // 플래너, 유저 매칭은 최대 한 개까지만 생성 가능
        if (!matches.isEmpty()){
            throw new BadRequestException(BaseException.MATCHING_ALREADY_EXIST);
        }
        matchJPARepository.save(
            Match.builder()
                    .couple(couple)
                    .planner(planner)
                    .chat(chat)
                    .price(0L)
                    .build()
        );
    }

    public MatchResponse.FindAllWithNoReviewDTO findMatchesWithNoReview(User user) {
        if (user.getDtype().equals(Role.PLANNER.getRoleName())) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
        Couple couple = coupleJPARepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );

        List<Match> matches = matchJPARepository.findAllByCouple(couple);
        List<Match> confirmedMatches = getConfirmedMatches(matches);
        List<Match> matchesWithNoReview = getMatchesWithNoReview(confirmedMatches);

        List<MatchResponse.MatchDTO> matchDTOS = MatchDTOConverter.toFindAllWithNoReviewDTO(matchesWithNoReview);

        return new MatchResponse.FindAllWithNoReviewDTO(matchDTOS);
    }

    // Match Update : 확정 상태, 가격, 확정 날짜
    @Transactional
    public void confirmAll(User user, Long chatId) {
        Match match = matchJPARepository.findByChatId(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        // 유저 본인의 채팅방이 맞는지 확인
        permissionCheck(user.getDtype(), user.getId(), match);

        // 플래너가 1개씩 전부 확정한 후에 예비 부부가 전체 확정 가능
        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);

        // 모든 견적서 확정 완료 시
        if (isAllConfirmed(quotations)) {
            match.updateStatusConfirmed();
            match.updateConfirmedPrice(match.getPrice());
            matchJPARepository.save(match);
            // 견적서 전체 확정 후 플래너 포트폴리오의 avg, min, max price 업데이트 하기
            portfolioService.updateConfirmedPrices(match.getPlanner());
        }
        else {
            // 확정되지 않은 견적서가 있을 때 에러 반환
            throw new BadRequestException(BaseException.QUOTATIONS_NOT_ALL_CONFIRMED);
        }
    }


    private Boolean isAllConfirmed(List<Quotation> quotations) {
        if (quotations.isEmpty()) {
            throw new BadRequestException(BaseException.QUOTATION_NOTHING_TO_CONFIRM);
        }
        else {
            // 모든 견적서 확정 됐는지 여부 구하기
            return quotations.stream().allMatch(quotation -> quotation.getStatus().equals(QuotationStatus.CONFIRMED));
        }
    }

    private void permissionCheck(String role, Long userId, Match match) {
        Boolean isPlanner = role.equals(Role.PLANNER.getRoleName());
        Boolean isInvalidUser = isPlanner ? !match.getPlanner().getId().equals(userId)
                : !match.getCouple().getId().equals(userId);

        if (isInvalidUser){
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
    }

    private List<Match> getConfirmedMatches(List<Match> matches) {
        return matches.stream()
                .filter(match -> match.getStatus().equals(MatchStatus.CONFIRMED))
                .toList();
    }

    private List<Match> getMatchesWithNoReview(List<Match> matches) {
        return matches.stream()
                .filter(match -> match.getReviewStatus().equals(ReviewStatus.UNWRITTEN))
                .toList();
    }
}