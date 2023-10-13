package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
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

    @Transactional
    public void insertQuotation(Pair<String, Long> info, Long matchId, QuotationRequest.add request) {
        Match match = getMatchByIdAndPlannerId(info, matchId);

        if (match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new ForbiddenException(BaseException.MATCHING_ALREADY_CONFIRMED);
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

    public QuotationResponse.findAllByMatchId findQuotationsByMatchId(Long matchId) {
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        MatchStatus status = match.getStatus();

        List<QuotationResponse.QuotationDTO> quotationDTOS = QuotationDTOConverter.toFindByMatchIdDTO(quotations);

        return new QuotationResponse.findAllByMatchId(status.toString(), match.getPrice(), match.getConfirmedPrice(), quotationDTOS);
    }

    @Transactional
    public void confirm(Pair<String, Long> info, Long matchId, Long quotationId) {
        Match match = getMatchByIdAndPlannerId(info, matchId);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        checkQuotationEditable(quotation);

        // 확정 가격 업데이트
        Long previousConfirmedPrice = match.getConfirmedPrice();
        match.updateConfirmedPrice(previousConfirmedPrice + quotation.getPrice());
        matchJPARepository.save(match);

        quotation.updateStatus(QuotationStatus.CONFIRMED);
        quotationJPARepository.save(quotation);
    }

    @Transactional
    public void update(Pair<String, Long> info, Long matchId, Long quotationId, QuotationRequest.update request) {
        Match match = getMatchByIdAndPlannerId(info, matchId);

        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        Quotation quotation = getQuotationById(quotationId, quotations);

        checkQuotationEditable(quotation);

        Boolean isPriceChanged = (!quotation.getPrice().equals(request.price()));

        quotation.update(request);
        quotationJPARepository.save(quotation);

        if (isPriceChanged) {
            Long price = PriceCalculator.calculateQuotationPrice(quotations);
            match.updatePrice(price);
            matchJPARepository.save(match);
        }
    }

    private Match getMatchByIdAndPlannerId(Pair<String, Long> info, Long matchId) {
        // 매칭 내역이 존재하지 않을 때는 404 에러를 내보내야 하고
        // 해당 매칭 내역에 접근할 수 없다면 403 에러를 내보내야 하기 때문에
        // 매칭 ID 로만 조회 후 권한 체크
        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        Long plannerId = info.getSecond();
        if (!match.getPlanner().getId().equals(plannerId)) {
            throw new ForbiddenException(BaseException.QUOTATION_ACCESS_DENIED);
        }

        return match;
    }

    private static Quotation getQuotationById(Long quotationId, List<Quotation> quotations) {
        return quotations
                .stream()
                .filter(iter -> Objects.equals(iter.getId(), quotationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(BaseException.QUOTATION_NOT_FOUND));
    }

    private static void checkQuotationEditable(Quotation quotation) {
        if (quotation.getStatus().equals(QuotationStatus.CONFIRMED)) {
            throw new ForbiddenException(BaseException.QUOTATION_ALREADY_CONFIRMED);
        }
    }
}
