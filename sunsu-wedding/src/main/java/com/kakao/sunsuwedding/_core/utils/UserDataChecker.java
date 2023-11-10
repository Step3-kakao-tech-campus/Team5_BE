package com.kakao.sunsuwedding._core.utils;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding.user.UserRequest;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.email.EmailCode;
import com.kakao.sunsuwedding.user.email.EmailCodeJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.kakao.sunsuwedding.user.email.EmailServiceImpl.AUTHENTICATION_EXP;

@Component
@RequiredArgsConstructor
public class UserDataChecker {
    private final UserJPARepository userJPARepository;
    private final EmailCodeJPARepository emailCodeJPARepository;
    private final PasswordEncoder passwordEncoder;

    public void checkPasswordIsSame(String password, String password2) {
        boolean isEqual = Objects.equals(password, password2);
        if (!isEqual){
            throw new BadRequestException(BaseException.USER_PASSWORD_NOT_SAME);
        }
    }

    public void checkEmailAlreadyExist(String email) {
        Optional<User> userOptional = userJPARepository.findByEmailNative(email);
        if (userOptional.isPresent()){
            throw new BadRequestException(BaseException.USER_EMAIL_EXIST);
        }
    }

    public void checkEmailAuthenticated(UserRequest.SignUpDTO requestDTO) {
        EmailCode emailCode = emailCodeJPARepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new BadRequestException(BaseException.UNAUTHENTICATED_EMAIL));

        if (emailCode.getConfirmed().equals(false)) {
            throw new BadRequestException(BaseException.UNAUTHENTICATED_EMAIL);
        }

        // 인증 지속시간은 30분. 30분 초과 시 재인증 필요
        Duration duration = Duration.between(emailCode.getCreatedAt(), LocalDateTime.now());
        if (duration.getSeconds() > AUTHENTICATION_EXP) {
            emailCodeJPARepository.delete(emailCode);
            throw new BadRequestException(BaseException.AUTHENTICATION_EXPIRED);
        }
    }

    public void verifyPassword(UserRequest.LoginDTO requestDTO, User user) {
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new BadRequestException(BaseException.USER_PASSWORD_WRONG);
        }
    }
}
