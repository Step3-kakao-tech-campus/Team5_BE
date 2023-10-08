package com.kakao.sunsuwedding.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Grade {
    NORMAL("normal"),
    PREMIUM("premium");

    @Getter
    private final String gradeName;
}
