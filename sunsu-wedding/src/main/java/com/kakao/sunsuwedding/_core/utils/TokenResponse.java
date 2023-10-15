package com.kakao.sunsuwedding._core.utils;

import com.kakao.sunsuwedding._core.errors.exception.TokenException;

public record TokenResponse(
        boolean success,
        String response,
        TokenError error
) {
    public record TokenError(String code, String message) {}

    public static TokenResponse error(TokenException tokenException) {
        return new TokenResponse(
                false,
                null,
                new TokenError(tokenException.getCode(), tokenException.getMessage())
        );
    }
}
