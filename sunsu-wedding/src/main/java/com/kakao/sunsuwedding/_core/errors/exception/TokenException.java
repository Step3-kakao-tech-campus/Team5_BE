package com.kakao.sunsuwedding._core.errors.exception;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.CustomException;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import org.springframework.http.HttpStatus;

public class TokenException extends RuntimeException implements CustomException {
    private final BaseException exception;

    public TokenException(BaseException exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApiUtils.ApiResult<?> body() {
        return ApiUtils.error(exception);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }

    public int code() {
        return exception.getCode();
    }
}
