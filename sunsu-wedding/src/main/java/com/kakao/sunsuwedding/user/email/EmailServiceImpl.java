package com.kakao.sunsuwedding.user.email;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
import com.kakao.sunsuwedding._core.utils.UserDataChecker;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserDataChecker userDataChecker;
    private final JavaMailSenderImpl javaMailSender;
    private final EmailCodeJPARepository emailCodeJPARepository;

    private final static int CODE_LENGTH = 6;

    private final static int CODE_EXP = 60 * 10;

    String EMAIL_CONTENT = """
                            <body style="margin: 0 0;">
                                <div style="vertical-align: middle; text-align: center; font-size: 14px; color: black; margin: 0 0; padding: 0 20px 100px 20px; background-image: linear-gradient(to top, rgba(167, 207, 255, 0.05), rgba(167, 207, 255, 0.5));">
                                    <img style="padding: 100px 0; width: 60vw; max-width: 350px; display: block; margin: 0 auto;" alt="순수웨딩" src="https://raw.githubusercontent.com/Step3-kakao-tech-campus/Team5_FE/7a7f8a31b18f4dd6c835101c3db7b903ef1b9cc6/src/assets/logo-01.svg" />\s
                                    <p style="margin: 0 0;">순수웨딩 회원가입 이메일 인증코드입니다.</p>
                                    <h2 style="margin: 50px 0;">인증코드: <span style="color: #0073C2;">%s</span></h2>
                                    <p>해당 인증코드는 10분 이내 1회만 사용 가능합니다.<br/>이후에는 인증코드를 다시 요청하셔야 합니다.</p>
                                    <p>감사합니다.</p>
                                    <p style="margin-bottom: 0;">- 순수웨딩 -</p>
                                </div>
                            </body>
                            """;

    @Value("${email.username}")
    private String sender;

    @Value("${email.test-code}")
    private Long TEST_CODE;

    @Transactional
    public void send(EmailRequest.SendCode request) {
        userDataChecker.checkEmailAlreadyExist(request.email());

        // 크램폴린 내부 정책으로 인한 smtp 프로토콜 사용 불가로
        // 이메일 인증 코드를 테스트 코드로 대체 및 이메일 발송 제외
//        String code = generateCode();
        String code = TEST_CODE.toString();
//        MimeMessage email = createMail(request.email(), code);
//        javaMailSender.send(email);

        EmailCode EMailCode = findMailCodeByRequest(request);

        EMailCode.setCode(code);
        EMailCode.setEmail(request.email());
        EMailCode.setConfirmed(false);
        EMailCode.setCreatedAt(LocalDateTime.now());

        emailCodeJPARepository.save(EMailCode);
    }

    public void verify(EmailRequest.CheckCode request) {
        EmailCode EMailCode = findMailCodeByRequest(request);

        checkCodeExpiration(EMailCode);
        checkEmailAlreadyAuthenticated(EMailCode);
        matchCode(request, EMailCode);

        EMailCode.setConfirmed(true);
        emailCodeJPARepository.save(EMailCode);
    }

    private EmailCode findMailCodeByRequest(EmailRequest.SendCode request) {
        return emailCodeJPARepository.findByEmail(request.email())
                .orElseGet(() -> EmailCode.builder().build());
    }

    private EmailCode findMailCodeByRequest(EmailRequest.CheckCode request) {
        return emailCodeJPARepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(BaseException.CODE_NOT_FOUND));
    }

    private static void matchCode(EmailRequest.CheckCode request, EmailCode EMailCode) {
        if (!Objects.equals(EMailCode.getCode(), request.code())) {
            throw new BadRequestException(BaseException.CODE_NOT_MATCHED);
        }
    }

    private void checkCodeExpiration(EmailCode EMailCode) {
        Duration duration = Duration.between(EMailCode.getCreatedAt(), LocalDateTime.now());
        if (duration.getSeconds() > CODE_EXP) {
            emailCodeJPARepository.delete(EMailCode);
            throw new BadRequestException(BaseException.CODE_EXPIRED);
        }
    }

    private static void checkEmailAlreadyAuthenticated(EmailCode EMailCode) {
        if (EMailCode.confirmed.equals(true)) {
            throw new BadRequestException(BaseException.EMAIL_ALREADY_AUTHENTICATED);
        }
    }

    private String generateCode() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            IntStream.range(0, CODE_LENGTH)
                    .forEach(i -> builder.append(random.nextInt(10)));

            return builder.toString();
        }
        catch (NoSuchAlgorithmException exception) {
            throw new ServerException(BaseException.CODE_GENERATE_ERROR);
        }
    }

    private MimeMessage createMail(String receiver, String code) {
        MimeMessage email = javaMailSender.createMimeMessage();

        try {
            email.setFrom(sender);
            email.setRecipients(MimeMessage.RecipientType.TO, receiver);
            email.setSubject("순수웨딩 회원가입 이메일 인증 코드");

            String body = String.format(EMAIL_CONTENT, code);
            email.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new ServerException(BaseException.EMAIL_GENERATE_ERROR);
        }

        return email;
    }
}
