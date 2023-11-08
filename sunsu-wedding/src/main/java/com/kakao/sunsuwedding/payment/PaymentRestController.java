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
@RequestMapping("/api/payment")
public class PaymentRestController {

    private final PaymentServiceImpl paymentServiceImpl;

    // 결제 정보 저장 (프론트 결제 요청 전)
    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody @Valid PaymentRequest.SaveDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        paymentServiceImpl.save(userDetails.getUser().getId(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 유저 통합 승인
    @PostMapping("/approve")
    public ResponseEntity<?> approve(
            @RequestBody @Valid PaymentRequest.ApproveDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        paymentServiceImpl.approve(userDetails.getUser().getId(), requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
