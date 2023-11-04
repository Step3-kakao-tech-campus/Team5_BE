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
public class QuotationService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    @Transactional
    public void insertQuotation(User user, Long chatId, QuotationRequest.Add request) {
        Match match = getMatchByChatIdAndPlannerId(user, chatId);

        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.MATCHING_ALREADY_CONFIRMED);
        }

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
        Match match = matchJPARepository.findByChatId(chatId)
                .orElseThrow(() -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        MatchStatus status = match.getStatus();

        List<QuotationResponse.QuotationDTO> quotationDTOS = QuotationDTOConverter.toFindByMatchIdDTO(quotations);

        return new QuotationResponse.FindAllByMatchId(status.toString(), match.getPrice(), match.getConfirmedPrice(), quotationDTOS);
    }

    // 견적서 모아보기
    public QuotationResponse.FindByUserDTO findQuotationsByUser(User user, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Quotation> pageContent = getQuotationsByUser(user.getDtype(), user.getId(), pageable);
        List<Quotation> quotations = pageContent.getContent();

        Map<Long, List<Quotation>> quotationsByChatId = quotations.stream().collect(
                Collectors.groupingBy(quotation -> quotation.getMatch().getChat().getId())
        );
        List<Long> chatIds = quotationsByChatId.keySet().stream().toList();

        List<QuotationResponse.QuotationsByChatIdDTO> quotationsByChatIdDTOS =
                QuotationDTOConverter.toQuotationsByChatIdDTO(quotationsByChatId, chatIds, user.getDtype());

        return new QuotationResponse.FindByUserDTO(quotationsByChatIdDTOS);
    }

    @Transactional
    public void confirm(User user, Long chatId, Long quotationId) {
        Match match = getMatchByChatIdAndPlannerId(user, chatId);

        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.QUOTATION_ALREADY_CONFIRMED);
        }

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        quotation.updateStatus(QuotationStatus.CONFIRMED);
        quotationJPARepository.save(quotation);

        // 확정 가격 업데이트
        Long confirmedPrice = PriceCalculator.calculateConfirmedQuotationPrice(quotations);
        match.updateConfirmedPrice(confirmedPrice);
        matchJPARepository.save(match);
    }

    @Transactional
    public void update(User user, Long chatId, Long quotationId, QuotationRequest.Update request) {
        Match match = getMatchByChatIdAndPlannerId(user, chatId);

        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.QUOTATION_ALREADY_CONFIRMED);
        }

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        Boolean isPriceChanged = (!quotation.getPrice().equals(request.price()));

        quotation.update(request);
        quotationJPARepository.save(quotation);

        if (isPriceChanged) {
            Long price = PriceCalculator.calculateQuotationPrice(quotations);
            match.updatePrice(price);
            matchJPARepository.save(match);
        }
    }

    @Transactional
    public void deleteQuotation(User user, Long quotationId) {
        Quotation quotation = quotationJPARepository.findById(quotationId).orElseThrow(
                () -> new NotFoundException(BaseException.QUOTATION_NOT_FOUND)
        );

        if (!quotation.getMatch().getPlanner().getId().equals(user.getId())) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }

        quotationJPARepository.delete(quotation);
    }

    private Match getMatchByChatIdAndPlannerId(User user, Long chatId) {
        // 매칭 내역이 존재하지 않을 때는 404 에러를 내보내야 하고
        // 해당 매칭 내역에 접근할 수 없다면 403 에러를 내보내야 하기 때문에
        // 매칭 ID 로만 조회 후 권한 체크
        Match match = matchJPARepository.findByChatId(chatId)
                .orElseThrow(() -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        if (!match.getPlanner().getId().equals(user.getId())) {
            throw new ForbiddenException(BaseException.QUOTATION_ACCESS_DENIED);
        }

        return match;
    }

    private Quotation getQuotationById(Long quotationId, List<Quotation> quotations) {
        Quotation quotation = quotations
                .stream()
                .filter(iter -> Objects.equals(iter.getId(), quotationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(BaseException.QUOTATION_NOT_FOUND));

        if (quotation.getStatus().equals(QuotationStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.QUOTATION_ALREADY_CONFIRMED);
        }

        return quotation;
    }

    private Page<Quotation> getQuotationsByUser(String role, Long id, Pageable pageable) {
        return role.equals(Role.PLANNER.getRoleName()) ?
                quotationJPARepository.findAllByMatchPlannerIdOrderByModifiedAtDesc(id, pageable) :
                quotationJPARepository.findAllByMatchCoupleIdOrderByModifiedAtDesc(id, pageable);
    }
}
