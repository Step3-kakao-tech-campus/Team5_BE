package com.kakao.sunsuwedding.Quotation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuotationStatus {
    CONFIRMED("완료"),
    UNCONFIRMED("미완료");

    private final String status;

    public String toString() {
        return status;
    }
}
