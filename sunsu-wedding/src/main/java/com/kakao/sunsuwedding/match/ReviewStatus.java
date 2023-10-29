package com.kakao.sunsuwedding.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewStatus {
    WRITTEN("작성"),
    UNWRITTEN("미작성");

    @Getter
    private final String reviewStatus;

    public String toString() {
        return reviewStatus;
    }
}
