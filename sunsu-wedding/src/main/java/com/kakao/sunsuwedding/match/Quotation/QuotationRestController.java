package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.match.MatchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quotations")
public class QuotationRestController {
    private final QuotationService quotationService;
    private final MatchService matchService;

    @PostMapping("")
    public ResponseEntity<?> createQuotation(@RequestParam Long matchId,
                                             @Valid @RequestBody QuotationRequest.addQuotation request) {
        quotationService.insertQuotation(matchId, request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("")
    public ResponseEntity<?> findQuotations(@RequestParam @Min(1) Long matchId) {
        QuotationResponse.findAllByMatchId response = quotationService.findQuotationsByMatchId(matchId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("/confirmAll/{matchId}")
    public ResponseEntity<?> confirmAll(@PathVariable Long matchId) {
        matchService.confirmAll(matchId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/confirm/{quotationId}")
    public ResponseEntity<?> confirm(@PathVariable Long quotationId,
                                     @RequestParam Long matchId) {
        quotationService.confirm(matchId, quotationId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
