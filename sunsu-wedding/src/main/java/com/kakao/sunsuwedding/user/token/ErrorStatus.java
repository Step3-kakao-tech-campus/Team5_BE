package com.kakao.sunsuwedding.user.token;

import com.kakao.sunsuwedding._core.errors.BaseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {
    ACCESS_TOKEN_ALIVE("ACCESS_TOKEN_ALIVE", BaseException.ACCESS_TOKEN_STILL_ALIVE.getMessage()),
    LOGIN_FAIL("LOGIN_FAIL", BaseException.LOGIN_FAIL.getMessage()),
    EXPIRED_TOKEN("EXPIRED_TOKEN", BaseException.ACCESS_TOKEN_EXPIRED.getMessage()),
    MISSING_TOKEN("MISSING_TOKEN", BaseException.TOKEN_NOT_FOUND.getMessage()),
    INVALID_TOKEN("INVALID_TOKEN", BaseException.TOKEN_NOT_VALID.getMessage()),
    ALL_TOKEN_EXPIRED("ALL_EXPIRED", BaseException.ALL_TOKEN_EXPIRED.getMessage());

    private final String code;
    private final String message;
}
