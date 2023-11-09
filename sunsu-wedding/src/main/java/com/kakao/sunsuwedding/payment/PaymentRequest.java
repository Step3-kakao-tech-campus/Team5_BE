package com.kakao.sunsuwedding.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
    public record SaveDTO(
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        String orderId,

        @NotNull(message = "금액은 비어있으면 안됩니다.")
        @Min(value = 0, message = "금액은 양수여야 합니다.")
        Long amount
    ) {}

    public record ApproveDTO(
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        String orderId,

        @NotEmpty(message = "paymentKey는 비어있으면 안됩니다.")
        String paymentKey,

        @NotNull(message = "금액은 비어있으면 안됩니다.")
        @Min(value = 0, message = "금액은 양수여야 합니다.")
        Long amount
    ) {}
}
