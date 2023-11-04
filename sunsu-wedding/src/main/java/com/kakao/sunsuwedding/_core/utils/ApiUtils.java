package com.kakao.sunsuwedding._core.utils;

import com.kakao.sunsuwedding._core.errors.BaseException;
import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(status.value(), message));
    }

    public static ApiResult<?> error(BaseException exception) {
        return new ApiResult<>(false, null, new ApiError(exception.getCode(), exception.getMessage()));
    }

    public record ApiResult<T>(
            boolean success,
            T response,
            ApiError error
    ) {
    }

    public record ApiError(
            int status,
            String message
    ) {
    }
}
