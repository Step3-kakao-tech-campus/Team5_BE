package com.kakao.sunsuwedding._core.errors.exception;

import com.kakao.sunsuwedding._core.utils.TokenResponse;
import com.kakao.sunsuwedding.user.token.ErrorStatus;
import lombok.Getter;

public class TokenException extends RuntimeException {
    @Getter
    String code;

    public TokenException(ErrorStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public TokenResponse body() {
        return TokenResponse.error(this);
    }
}
