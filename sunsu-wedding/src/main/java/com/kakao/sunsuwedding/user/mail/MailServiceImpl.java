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
public class MailServiceImpl implements MailService {
    private final UserDataChecker userDataChecker;
    private final JavaMailSenderImpl javaMailSender;
    private final MailCodeJPARepository mailCodeJPARepository;

    @Value("${email.username}")
    private String sender;

    @Transactional
    public void send(MailRequest.SendCode request) {
        userDataChecker.sameCheckEmail(request.email());

        String code = generateCode();
        MimeMessage email = createMail(request.email(), code);
        javaMailSender.send(email);

        MailCode mailCode = findMailCodeByRequest(request);

        mailCode.setCode(code);
        mailCode.setEmail(request.email());
        mailCode.setConfirmed(false);
        mailCode.setCreatedAt(LocalDateTime.now());

        mailCodeJPARepository.save(mailCode);
    }

    public void verify(MailRequest.CheckCode request) {
        MailCode mailCode = findMailCodeByRequest(request);

        checkCodeExpiration(mailCode);
        matchCode(request, mailCode);

        mailCode.setConfirmed(true);
        mailCodeJPARepository.save(mailCode);
    }

    private MailCode findMailCodeByRequest(MailRequest.SendCode request) {
        return mailCodeJPARepository.findByEmail(request.email())
                .orElseGet(() -> MailCode.builder().build());
    }

    private MailCode findMailCodeByRequest(MailRequest.CheckCode request) {
        return mailCodeJPARepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(BaseException.CODE_NOT_FOUND));
    }

    private static void matchCode(MailRequest.CheckCode request, MailCode mailCode) {
        if (!Objects.equals(mailCode.getCode(), request.code())) {
            throw new BadRequestException(BaseException.CODE_NOT_MATCHED);
        }
    }

    private void checkCodeExpiration(MailCode mailCode) {
        Duration duration = Duration.between(mailCode.getCreatedAt(), LocalDateTime.now());
        if (duration.getSeconds() > CODE_EXP) {
            mailCodeJPARepository.delete(mailCode);
            throw new BadRequestException(BaseException.CODE_EXPIRED);
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
