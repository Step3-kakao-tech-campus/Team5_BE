package com.kakao.sunsuwedding.match.Quotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuotationStatus {
    CONFIRMED("확정"),
    UNCONFIRMED("미확정");

    @Getter
    private final String status;
}
