package com.kakao.sunsuwedding._core.errors.exception;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.CustomException;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 데이터를 찾을 수 없음 404
@Getter
public class NotFoundException extends RuntimeException implements CustomException {
    private final BaseException exception;

    public NotFoundException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
    }
    
    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(exception);
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }

    public int code() {
        return exception.getCode();
    }
}