package com.kakao.sunsuwedding.quotation;

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
