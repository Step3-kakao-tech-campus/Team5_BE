package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.match.MatchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
                                             @RequestParam @Min(1) Long matchId,
                                             @Valid @RequestBody QuotationRequest.Add request) {
        quotationService.insertQuotation(userDetails.getInfo(), matchId, request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("")
    public ResponseEntity<?> findQuotations(@RequestParam @Min(1) Long matchId) {
        QuotationResponse.FindAllByMatchId response = quotationService.findQuotationsByMatchId(matchId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("/confirmAll")
    public ResponseEntity<?> confirmAll(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam @Min(1) Long matchId) {
        matchService.confirmAll(userDetails.getInfo(), matchId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/confirm/{quotationId}")
    public ResponseEntity<?> confirm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @PathVariable @Min(1) Long quotationId,
                                     @RequestParam @Min(1) Long matchId) {
        quotationService.confirm(userDetails.getInfo(), matchId, quotationId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PutMapping("/{quotationId}")
    public ResponseEntity<?> updateQuotation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Min(1) Long quotationId,
                                             @RequestParam @Min(1) Long matchId,
                                             @Valid @RequestBody QuotationRequest.Update request) {
        quotationService.update(userDetails.getInfo(), matchId, quotationId, request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
