package com.kakao.sunsuwedding._core.errors;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import org.springframework.http.HttpStatus;

public interface CustomException {
    ApiUtils.ApiResult<?> body();
    HttpStatus status();
    int code();
}
