package com.kakao.sunsuwedding.user.mail;

import com.kakao.sunsuwedding._core.errors.BaseException;
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
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MailService {
    private final UserDataChecker userDataChecker;
    private final JavaMailSenderImpl javaMailSender;
    private final MailJPARepository mailJPARepository;

    private final static int CODE_LENGTH = 6;

    @Value("${email.username}")
    private String sender;

    @Transactional
    public void send(MailRequest.SendCode request) {
        userDataChecker.sameCheckEmail(request.email());

        String code = generateCode();
        MimeMessage email = createMail(request.email(), code);
        javaMailSender.send(email);

        mailJPARepository.save(
                Mail.builder()
                        .code(code)
                        .eamil(request.email())
                        .build()
        );
    }

    public void verify(String code) {

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

            String body =
                    "<h3>" + "요청하신 인증코드 입니다." + "</h3>" +
                    "<h1>" + code + "</h1>" +
                    "<h3>" + "감사합니다." + "</h3>";

            email.setText(body);
        } catch (MessagingException e) {
            throw new ServerException(BaseException.EMAIL_GENERATE_ERROR);
        }

        return email;
    }
}
