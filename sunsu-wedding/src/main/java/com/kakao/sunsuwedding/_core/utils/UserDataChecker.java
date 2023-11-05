package com.kakao.sunsuwedding._core.utils;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDataChecker {
    private final UserJPARepository userJPARepository;

    public void sameCheckPassword(String password, String password2) {
        boolean isEqual = Objects.equals(password, password2);
        if (!isEqual){
            throw new BadRequestException(BaseException.USER_PASSWORD_NOT_SAME);
        }
    }

    public void sameCheckEmail(String email) {
        Optional<User> userOptional = userJPARepository.findByEmailNative(email);
        if (userOptional.isPresent()){
            throw new BadRequestException(BaseException.USER_EMAIL_EXIST);
        }
    }
}
