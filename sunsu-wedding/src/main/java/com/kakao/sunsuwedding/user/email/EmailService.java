package com.kakao.sunsuwedding.user.email;

public interface EmailService {

    void send(EmailRequest.SendCode request);

    void verify(EmailRequest.CheckCode request);
}
