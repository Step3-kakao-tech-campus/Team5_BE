package com.kakao.sunsuwedding._core.errors.exception;


import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 인증 안됨
@Getter
public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }

    public Object body(){
        return ApiUtils.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }
}