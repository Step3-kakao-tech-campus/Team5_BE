package com.kakao.sunsuwedding.user.mail;

public interface MailService {

    int CODE_LENGTH = 6;

    int CODE_EXP = 60 * 10;

    String EMAIL_CONTENT = """
                            <div style="vertical-align: middle; text-align: center; font-size: 12px; color: black; margin: 0 0; padding: 0 20px 100px 20px; background-image: linear-gradient(to top, rgba(167, 207, 255, 0.05), rgba(167, 207, 255, 0.5));">
                                <img style="padding: 100px 0; width: 60vw; max-width: 350px; display: block; margin: 0 auto;" alt="순수웨딩" src="https://raw.githubusercontent.com/Step3-kakao-tech-campus/Team5_FE/7a7f8a31b18f4dd6c835101c3db7b903ef1b9cc6/src/assets/logo-01.svg" />\s
                                <p style="margin: 0 0;">순수웨딩 회원가입 이메일 인증코드입니다.</p>
                                <h2 style="margin: 50px 0;">인증코드: <span style="color: #0073C2;">%s</span></h2>
                                <p>해당 인증코드는 10분 이내 1회만 사용 가능합니다.<br/>이후에는 인증코드를 다시 요청하셔야 합니다.</p>
                                <p>감사합니다.</p>
                                <p style="margin-bottom: 0;">- 순수웨딩 -</p>
                            </div>
                            """;


    void send(MailRequest.SendCode request);

    void verify(MailRequest.CheckCode request);
}
