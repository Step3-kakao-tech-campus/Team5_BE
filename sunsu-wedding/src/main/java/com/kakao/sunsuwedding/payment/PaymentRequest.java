package com.kakao.sunsuwedding.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class PaymentRequest {

    @Getter
    @Setter
    public static class SaveDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        @Min(value = 0, message = "금액은 양수여야 합니다.")
        private Long amount;
    }

    @Getter
    @Setter
    public static class ConfirmDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        @NotEmpty(message = "paymentKey는 비어있으면 안됩니다.")
        private String paymentKey;

        @Min(value = 0, message = "금액은 양수여야 합니다.")
        private Long amount;
    }

    @Getter
    @Setter
    public static class UpgradeDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        @NotEmpty(message = "paymentKey는 비어있으면 안됩니다.")
        private String paymentKey;

        @NotEmpty(message = "상태값은 비어있으면 안됩니다.")
        private String status;

        @Min(value = 0, message = "금액은 양수여야 합니다.")
        private Long amount;
    }

    @Getter
    @Setter
    public static class ApproveDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        @NotEmpty(message = "paymentKey는 비어있으면 안됩니다.")
        private String paymentKey;

        @Min(value = 0, message = "금액은 양수여야 합니다.")
        private Long amount;
    }

}
