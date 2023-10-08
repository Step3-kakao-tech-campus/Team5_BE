package com.kakao.sunsuwedding.match.Quotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuotationStatus {
    CONFIRMED("완료"),
    UNCONFIRMED("미완료");

    @Getter
    private final String status;
}
