package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception403;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuotationService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    public void insertQuotation(Pair<String, Long> info, Long matchId, QuotationRequest.add request) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

        // 해당 매칭 내역의 플래너와 견적서 추가를 요청한 플래너가 같은지 검사
        checkPermission(info, match);

        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new Exception403(BaseException.MATCHING_ALREADY_CONFIRMED.getMessage());
        }

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

    public QuotationResponse.findAllByMatchId findQuotationsByMatchId(Long matchId) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        MatchStatus status = match.getStatus();

        List<QuotationResponse.QuotationDTO> quotationDTOS = QuotationDTOConverter.toFindByMatchIdDTO(quotations);

        return new QuotationResponse.findAllByMatchId(status.toString(), match.getPrice(), match.getConfirmedPrice(), quotationDTOS);
    }

    public void confirm(Pair<String, Long> info, Long matchId, Long quotationId) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

        // 해당 매칭 내역의 플래너와 견적서 추가를 요청한 플래너가 같은지 검사
        checkPermission(info, match);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        if (quotation.getStatus().equals(QuotationStatus.CONFIRMED)) {
            throw new Exception403(BaseException.QUOTATION_ALREADY_CONFIRMED.getMessage());
        }
        
        quotation.updateStatus(QuotationStatus.CONFIRMED);
        quotationJPARepository.save(quotation);
    }

    @Transactional
    public void update(Pair<String, Long> info, Long matchId, Long quotationId, QuotationRequest.update request) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

        checkPermission(info, match);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        if (quotation.getStatus().equals(QuotationStatus.CONFIRMED)) {
            throw new Exception403(BaseException.QUOTATION_CHANGE_DENIED.getMessage());
        }

        Boolean isPriceChanged = (!quotation.getPrice().equals(request.price()));

        quotation.update(request);
        quotationJPARepository.save(quotation);

        if (isPriceChanged) {
            Long price = PriceCalculator.calculateQuotationPrice(quotations);
            match.updatePrice(price);
            matchJPARepository.save(match);
        }
    }

    private static void checkPermission(Pair<String, Long> info, Match match) {
        Long plannerId = info.getSecond();
        if (!match.getPlanner().getId().equals(plannerId)) {
            throw new Exception403(BaseException.QUOTATION_ACCESS_DENIED.getMessage());
        }
    }

    private static Quotation getQuotationById(Long quotationId, List<Quotation> quotations) {
        return quotations
                .stream()
                .filter(iter -> Objects.equals(iter.getId(), quotationId))
                .findFirst()
                .orElseThrow(() -> new Exception404(BaseException.QUOTATION_NOT_FOUND.getMessage()));
    }

    private static Boolean checkQuotationConfirmed(List<Quotation> quotations) {
        return quotations
                .stream()
                .allMatch(iter -> iter.getStatus().equals(QuotationStatus.CONFIRMED));
    }
}
