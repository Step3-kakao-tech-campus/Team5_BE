package com.kakao.sunsuwedding._core.errors.exception;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.CustomException;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 서버 에러 500
@Getter
public class ServerException extends RuntimeException implements CustomException {
    private final BaseException exception;

    public ServerException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public int code() {
        return exception.getCode();
    }
}
