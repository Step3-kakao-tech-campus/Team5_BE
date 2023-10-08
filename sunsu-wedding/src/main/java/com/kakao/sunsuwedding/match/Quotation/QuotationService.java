package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception403;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding._core.utils.PriceCalculator;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuotationService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    public void insertQuotation(Long matchId, QuotationRequest.add request) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

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

        return new QuotationResponse.findAllByMatchId(status.toString(), quotationDTOS);
    }

    @Transactional
    public void confirm(Long matchId, Long quotationId) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);
        
        quotation.updateStatus(QuotationStatus.CONFIRMED);
        quotationJPARepository.save(quotation);

        Boolean isAllConfirmed = checkQuotationConfirmed(quotations);

        if (isAllConfirmed) {
            Long totalPrice = PriceCalculator.calculateQuotationPrice(quotations);
            match.updateStatusConfirmed(totalPrice);
            matchJPARepository.save(match);
        }
    }

    @Transactional
    public void update(Long matchId, Long quotationId, QuotationRequest.update request) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        if (quotation.getStatus().equals(QuotationStatus.CONFIRMED)) {
            throw new Exception403(BaseException.QUOTATION_CHANGE_DENIED.getMessage());
        }

        Boolean isPriceChanged = (quotation.getPrice().equals(request.price()));

        quotation.update(request);
        quotationJPARepository.save(quotation);

        if (isPriceChanged) {
            Long price = PriceCalculator.calculateQuotationPrice(quotations);
            match.updatePrice(price);
            matchJPARepository.save(match);
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
