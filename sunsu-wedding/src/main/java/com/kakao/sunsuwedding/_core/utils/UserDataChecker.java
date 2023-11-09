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

import java.util.Objects;
import java.util.Optional;

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
        EmailCode EMailCode = emailCodeJPARepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new BadRequestException(BaseException.UNAUTHENTICATED_EMAIL));

        if (EMailCode.getConfirmed().equals(false)) {
            throw new BadRequestException(BaseException.UNAUTHENTICATED_EMAIL);
        }
    }

    public void verifyPassword(UserRequest.LoginDTO requestDTO, User user) {
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new BadRequestException(BaseException.USER_PASSWORD_WRONG);
        }
    }
}
