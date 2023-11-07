package com.kakao.sunsuwedding.user.mail;

public interface MailService {
    int CODE_LENGTH = 6;
    int CODE_EXP = 60 * 30;
    String EMAIL_CONTENT = """
                            <div style="text-align: center; font-size: 1.2rem; color: black; background-color: rgb(231, 240, 254); padding: 100px 40px;">
                                <h1 style="padding: 0 0 50px 0; margin: 0 0;">순수웨딩</h1>
                                <p style="margin: 50px 0 0 0;">순수웨딩 회원가입 이메일 인증코드입니다.</p>
                                <h2 style="margin: 50px 0;">인증코드: <span style="color: #4299EC;">%s</span></h2>
                                <p>해당 인증코드는 30분 이내 1회만 사용 가능합니다.<br/>이후에는 인증코드를 다시 요청하셔야 합니다.</p>
                                <p>감사합니다.</p>
                                <p style="margin-bottom: 0;">- 순수웨딩 -</p>
                            </div>
                            """;


    void send(MailRequest.SendCode request);
    void verify(MailRequest.CheckCode request);
}
