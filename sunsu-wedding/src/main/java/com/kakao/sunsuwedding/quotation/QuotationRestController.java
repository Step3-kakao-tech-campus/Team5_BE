package com.kakao.sunsuwedding.quotation;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quotation")
public class QuotationRestController {
    private final QuotationServiceImpl quotationServiceImpl;

    @PostMapping("")
    public ResponseEntity<?> addQuotation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestParam @Min(1) Long chatId,
                                          @Valid @RequestBody QuotationRequest.Add request) {
        quotationServiceImpl.addQuotation(userDetails.getUser(), chatId, request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 사용자가 특정 채팅방에서 작성한 견적서 조회
    @GetMapping("")
    public ResponseEntity<?> findQuotationsByChatId(@RequestParam @Min(1) Long chatId) {
        QuotationResponse.FindAllByMatchId response = quotationServiceImpl.findQuotationsByChatId(chatId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 사용자가 이때까지 작성한 모든 견적서 조회
    @GetMapping("/all")
    public ResponseEntity<?> findQuotationsByUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam(defaultValue = "0") Integer page) {
        QuotationResponse.FindByUserDTO response = quotationServiceImpl.findQuotationsByUser(userDetails.getUser(), page);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PostMapping("/confirm/{quotationId}")
    public ResponseEntity<?> confirm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @PathVariable @Min(1) Long quotationId,
                                     @RequestParam @Min(1) Long chatId) {
        quotationServiceImpl.confirm(userDetails.getUser(), chatId, quotationId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PutMapping("/{quotationId}")
    public ResponseEntity<?> updateQuotation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Min(1) Long quotationId,
                                             @RequestParam @Min(1) Long chatId,
                                             @Valid @RequestBody QuotationRequest.Update request) {
        quotationServiceImpl.updateQuotation(userDetails.getUser(), chatId, quotationId, request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/{quotationId}")
    public ResponseEntity<?> deleteQuotation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Min(1) Long quotationId) {
        quotationServiceImpl.deleteQuotation(userDetails.getUser(), quotationId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
