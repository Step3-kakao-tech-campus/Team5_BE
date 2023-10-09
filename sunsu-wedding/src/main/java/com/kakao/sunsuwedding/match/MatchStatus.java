package com.kakao.sunsuwedding.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MatchStatus {

    CONFIRMED("완료"),
    UNCONFIRMED("미완료");

    @Getter
    private final String status;

    public String toString() {
        return status;
    }
}