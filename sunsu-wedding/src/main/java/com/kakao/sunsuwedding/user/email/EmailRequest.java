package com.kakao.sunsuwedding.user.email;

public class EmailRequest {
    public record SendCode(
            String email
    ) {}

    public record CheckCode(
            String email,
            String code
    ) {}
}
