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
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
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
    private final PortfolioService portfolioService;

    @Transactional
    public void addMatch(Couple couple, Planner planner, Chat chat) {
        List<Match> matches = matchJPARepository.findByCoupleAndPlanner(couple, planner);

        // 플래너, 유저 매칭은 최대 한 개까지만 생성 가능
        if (!matches.isEmpty()){
            throw new BadRequestException(BaseException.MATCHING_ALREADY_EXIST);
        }

        Match match = matchJPARepository.save(
                Match.builder()
                        .couple(couple)
                        .planner(planner)
                        .chat(chat)
                        .price(0L)
                        .build()
        );
    }

    // Match Update : 확정 상태, 가격, 확정 날짜
    @Transactional
    public void confirmAll(Pair<String, Long> info, Long chatId) {
        Match match = matchJPARepository.findByChatId(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        // 유저 본인의 채팅방이 맞는지 확인
        permissionCheck(info, match);
        // 플래너가 1개씩 전부 확정한 후에 예비 부부가 전체 확정 가능
        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Boolean isAllConfirmed = isAllConfirmed(quotations);

        // 모든 견적서 확정 완료 시
        if (isAllConfirmed) {
            match.updateStatusConfirmed();
            match.updateConfirmedPrice(match.getPrice());
            matchJPARepository.save(match);
            // 견적서 전체 확정 후 플래너 포트폴리오의 avg, min, max price 업데이트 하기
            portfolioService.updateConfirmedPrices(match.getPlanner());
        }
        // 확정되지 않은 견적서가 있을 때
        else {
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

    private void permissionCheck(Pair<String, Long> info, Match match) {
        String role = info.getFirst();
        Long id = info.getSecond();
        if (role.equals(Role.PLANNER.getRoleName())) {
            if (!match.getPlanner().getId().equals(id))
                throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
        else {
            if (!match.getCouple().getId().equals(id))
                throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
    }
}