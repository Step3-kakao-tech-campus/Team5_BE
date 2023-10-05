package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception403;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    public void insertQuotation(Pair<Role, Integer> info, Long matchId, QuotationRequest.addQuotation request) {
        if (!info.getFirst().getRoleName().equals(Role.PLANNER.getRoleName())) {
            throw new Exception403(BaseException.PERMISSION_DENIED_METHOD_ACCESS.getMessage());
        }

        Match match = matchJPARepository.findById(matchId)
                .orElseThrow(() -> new Exception400(BaseException.MATCHING_NOT_FOUND.getMessage()));

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
        return null;
    }
}
