package com.kakao.sunsuwedding.user.mail;

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
public class MailService {
    private final UserDataChecker userDataChecker;
    private final JavaMailSenderImpl javaMailSender;
    private final MailCodeJPARepository mailCodeJPARepository;

    private final static int CODE_LENGTH = 6;
    private final static int CODE_EXP = 60 * 30;

    @Value("${email.username}")
    private String sender;

    @Transactional
    public void send(MailRequest.SendCode request) {
        userDataChecker.sameCheckEmail(request.email());

        String code = generateCode();
        MimeMessage email = createMail(request.email(), code);
        javaMailSender.send(email);

        MailCode mailCode = mailCodeJPARepository.findByEmail(request.email())
                        .orElse(MailCode.builder().build());

        mailCode.setCode(code);
        mailCode.setEmail(request.email());
        mailCode.setCreatedAt(LocalDateTime.now());

        mailCodeJPARepository.save(mailCode);
    }

    public void verify(MailRequest.CheckCode request) {
        MailCode mailCode = mailCodeJPARepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(BaseException.CODE_NOT_FOUND));

        Duration duration = Duration.between(mailCode.getCreatedAt(), LocalDateTime.now());
        if (duration.getSeconds() > CODE_EXP) {
            mailCodeJPARepository.delete(mailCode);
            throw new BadRequestException(BaseException.CODE_EXPIRED);
        }

        if (!Objects.equals(mailCode.getCode(), request.code())) {
            throw new BadRequestException(BaseException.CODE_NOT_MATCHED);
        }

        mailCodeJPARepository.delete(mailCode);
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

            String body = String.format("""
                            <div style="text-align: center; font-size: 1.2rem;">
                                <h1 style="padding-top: 50px;">순수웨딩</h1>
                                <hr color="#EOEOEO"/>
                                <p style="margin: 50px 0 0 0">순수웨딩 회원가입 이메일 인증코드입니다.</p>
                                <h2 style="margin-bottom: 50px;">인증코드: <span style="color: #4299EC;">%s</span></h2>
                                <p>해당 인증코드는 30분 이내 1회만 사용 가능합니다.<br/>이후에는 인증코드를 다시 요청하셔야 합니다.</p>
                                <p>감사합니다.</p>
                                <p><b>- 순수웨딩 -</b></p>
                            </div>
                            """, code);

            email.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new ServerException(BaseException.EMAIL_GENERATE_ERROR);
        }

        return email;
    }
}
