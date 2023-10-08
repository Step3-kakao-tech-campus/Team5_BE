package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.match.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quotations")
public class QuotationRestController {
    private final QuotationService quotationService;
    private final MatchService matchService;

    @PostMapping("")
    public ResponseEntity<?> createQuotation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestParam Long matchId,
                                             @Valid @RequestBody QuotationRequest.addQuotation request) {
        quotationService.insertQuotation(userDetails.getInfo(), matchId, request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("")
    public ResponseEntity<?> findQuotations(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestParam Long matchId) {
        QuotationResponse.findAllByMatchId response = quotationService.findQuotationsByMatchId(matchId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("/confirmAll/{matchId}")
    public ResponseEntity<?> confirmAll(@PathVariable Long matchId) {
        matchService.confirmAll(matchId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
