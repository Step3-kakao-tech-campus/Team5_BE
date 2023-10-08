package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception403;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotationService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    public void insertQuotation(Pair<String, Long> info, Long matchId, QuotationRequest.addQuotation request) {
        if (!info.getFirst().equals(Role.PLANNER.getRoleName())) {
            throw new Exception403(BaseException.PERMISSION_DENIED_METHOD_ACCESS.getMessage());
        }

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
        QuotationStatus status = getEntireQuotationStatus(quotations);

        List<QuotationResponse.QuotationDTO> quotationDTOS = QuotationDTOConverter.toFindByMatchIdDTO(quotations);

        return new QuotationResponse.findAllByMatchId(status.toString(), quotationDTOS);
    }

    private static QuotationStatus getEntireQuotationStatus(List<Quotation> quotations) {
        QuotationStatus status = QuotationStatus.CONFIRMED;
        for (Quotation quotation : quotations) {
            if (quotation.getStatus().equals(QuotationStatus.UNCONFIRMED)) {
                status = QuotationStatus.UNCONFIRMED;
                break;
            }
        }
        return status;
    }
}
