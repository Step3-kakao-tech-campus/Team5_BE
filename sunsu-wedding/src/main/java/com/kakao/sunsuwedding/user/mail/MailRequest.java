package com.kakao.sunsuwedding.user.mail;

public class MailRequest {
    public record SendCode(
            String email
    ) {}

    public record CheckCode(
            String email,
            String code
    ) {}
}
