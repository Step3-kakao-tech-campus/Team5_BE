package com.kakao.sunsuwedding._core.errors.exception;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 유효성 검사 실패, 잘못된 파라메터 요청 400
@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(BaseException exception){
        super(exception.getMessage());
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}