package com.kakao.sunsuwedding.payment;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentRestController {

    private final PaymentService paymentService;

    // 결제 정보 저장 (프론트 결제 요청 전)
    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody @Valid PaymentRequest.SaveDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        paymentService.save(userDetails.getUser().getId(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 결제 정보 검증 (프론트 결제 요청 후)
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(
            @RequestBody @Valid PaymentRequest.ConfirmDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String response = paymentService.confirm(userDetails.getUser().getId(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 유저 업그레이드 (프론트 결제 승인 후)
    @PostMapping("/upgrade")
    public ResponseEntity<?> upgrade(
            @RequestBody @Valid PaymentRequest.UpgradeDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        paymentService.upgrade(userDetails.getUser().getId(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
