package com.kakao.sunsuwedding.quotation;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuotationServiceImpl implements QuotationService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    private final PriceCalculator priceCalculator;
    private final QuotationDTOConverter quotationDTOConverter;

    private final static int QUOTATION_PAGE_SIZE = 10;

    @Transactional
    public void addQuotation(User user, Long chatId, QuotationRequest.Add request) {
        Match match = findMatchByChatIdAndPlannerId(user, chatId);

        checkCoupleExist(match);
        checkMatchingConfirmed(match);

        // 기존 매칭 가격에서 해당 견적서 가격 업데이터
        Long previousPrice = match.getPrice();
        match.updatePrice(previousPrice + request.price());
        matchJPARepository.save(match);

        quotationJPARepository.save(
                Quotation.builder()
                        .match(match)
                        .title(request.title())
                        .price(request.price())
                        .company(request.company())
                        .description(request.description())
                        .build()
        );
    }

    public QuotationResponse.FindAllByMatchId findQuotationsByChatId(Long chatId) {
        Match match = findMatchByChatId(chatId);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        MatchStatus status = match.getStatus();

        List<QuotationResponse.QuotationDTO> quotationDTOS = quotationDTOConverter.toFindByMatchIdDTO(quotations);

        return new QuotationResponse.FindAllByMatchId(status.toString(), match.getPrice(), match.getConfirmedPrice(), quotationDTOS);
    }

    // 견적서 모아보기
    public QuotationResponse.FindByUserDTO findQuotationsByUser(User user, int page) {
        Pageable pageable = PageRequest.of(page, QUOTATION_PAGE_SIZE);

        Page<Quotation> pageContent = findQuotationsByUser(user.getDtype(), user.getId(), pageable);
        List<Quotation> quotations = pageContent.getContent();

        Map<Long, List<Quotation>> quotationsByChatId = quotations.stream().collect(
                Collectors.groupingBy(quotation -> quotation.getMatch().getChat().getId())
        );
        List<Long> chatIds = quotationsByChatId.keySet().stream().toList();

        List<QuotationResponse.QuotationsByChatIdDTO> quotationsByChatIdDTOS =
                quotationDTOConverter.toQuotationsByChatIdDTO(quotationsByChatId, chatIds, user.getDtype());

        return new QuotationResponse.FindByUserDTO(quotationsByChatIdDTOS);
    }

    @Transactional
    public void confirm(User user, Long chatId, Long quotationId) {
        Match match = findMatchByChatIdAndPlannerId(user, chatId);

        checkQuotationConfirmed(match);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = findQuotationById(quotationId, quotations);

        quotation.updateStatus(QuotationStatus.CONFIRMED);
        quotationJPARepository.save(quotation);

        // 확정 가격 업데이트
        Long confirmedPrice = priceCalculator.calculateConfirmedQuotationPrice(quotations);
        match.updateConfirmedPrice(confirmedPrice);
        matchJPARepository.save(match);
    }

    @Transactional
    public void updateQuotation(User user, Long chatId, Long quotationId, QuotationRequest.Update request) {
        Match match = findMatchByChatIdAndPlannerId(user, chatId);

        checkQuotationConfirmed(match);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = findQuotationById(quotationId, quotations);

        Boolean isPriceChanged = (!quotation.getPrice().equals(request.price()));

        quotation.update(request);
        quotationJPARepository.save(quotation);

        if (isPriceChanged) {
            Long price = priceCalculator.calculateQuotationPrice(quotations);
            match.updatePrice(price);
            matchJPARepository.save(match);
        }
    }

    @Transactional
    public void deleteQuotation(User user, Long quotationId) {
        Quotation quotation = findQuotationById(quotationId);

        if (!quotation.getMatch().getPlanner().getId().equals(user.getId())) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }

        quotationJPARepository.delete(quotation);
    }

    private Quotation findQuotationById(Long quotationId) {
        return quotationJPARepository.findById(quotationId).orElseThrow(
                () -> new NotFoundException(BaseException.QUOTATION_NOT_FOUND)
        );
    }

    private Match findMatchByChatId(Long chatId) {
        return matchJPARepository.findByChatId(chatId)
                .orElseThrow(() -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));
    }

    private Match findMatchByChatIdAndPlannerId(User user, Long chatId) {
        // 매칭 내역이 존재하지 않을 때는 404 에러를 내보내야 하고
        // 해당 매칭 내역에 접근할 수 없다면 403 에러를 내보내야 하기 때문에
        // 매칭 ID 로만 조회 후 권한 체크
        Match match = findMatchByChatId(chatId);

        checkQuotationAccessPermission(user, match);

        return match;
    }

    private Quotation findQuotationById(Long quotationId, List<Quotation> quotations) {
        Quotation quotation = quotations
                .stream()
                .filter(iter -> Objects.equals(iter.getId(), quotationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(BaseException.QUOTATION_NOT_FOUND));

        checkQuotationConfirmed(quotation);

        return quotation;
    }

    private Page<Quotation> findQuotationsByUser(String role, Long id, Pageable pageable) {
        return role.equals(Role.PLANNER.getRoleName()) ?
                quotationJPARepository.findAllByMatchPlannerIdOrderByModifiedAtDesc(id, pageable) :
                quotationJPARepository.findAllByMatchCoupleIdOrderByModifiedAtDesc(id, pageable);
    }
    
    private static void checkMatchingConfirmed(Match match) {
        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.MATCHING_ALREADY_CONFIRMED);
        }
    }

    private static void checkCoupleExist(Match match) {
        if (match.getCouple() == null) {
            throw new ForbiddenException(BaseException.MATCHING_USER_NOT_FOUND);
        }
    }

    private static void checkQuotationConfirmed(Match match) {
        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.QUOTATION_ALREADY_CONFIRMED);
        }
    }

    private static void checkQuotationConfirmed(Quotation quotation) {
        if (quotation.getStatus().equals(QuotationStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.QUOTATION_ALREADY_CONFIRMED);
        }
    }

    private static void checkQuotationAccessPermission(User user, Match match) {
        if (!match.getPlanner().getId().equals(user.getId())) {
            throw new ForbiddenException(BaseException.QUOTATION_ACCESS_DENIED);
        }
    }
}
