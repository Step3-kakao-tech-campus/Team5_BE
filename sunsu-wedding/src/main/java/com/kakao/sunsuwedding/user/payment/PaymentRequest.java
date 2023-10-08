package com.kakao.sunsuwedding.user.payment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class PaymentRequest {

    @Getter @Setter
    public static class SaveDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        private Long amount;
    }

    @Getter @Setter
    public static class ConfirmDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        private Long amount;
    }

    @Getter @Setter
    public static class UpgradeDTO{
        @NotEmpty(message = "orderId는 비어있으면 안됩니다.")
        private String orderId;

        @NotEmpty(message = "상태값은 비어있으면 안됩니다.")
        private String status;

        private Long amount;
    }
}
