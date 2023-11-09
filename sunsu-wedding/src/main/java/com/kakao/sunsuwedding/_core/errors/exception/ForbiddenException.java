package com.kakao.sunsuwedding._core.errors.exception;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.CustomException;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 권한 없음 403
@Getter
public class ForbiddenException extends RuntimeException implements CustomException {
    private final BaseException exception;

    public ForbiddenException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }
    
    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception);
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }

    public int code() {
        return exception.getCode();
    }
}