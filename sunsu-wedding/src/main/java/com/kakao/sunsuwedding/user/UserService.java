package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final JWTProvider jwtProvider;

    @Transactional
    public void signup(UserRequest.SignUpDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());
        sameCheckPassword(requestDTO.getPassword(), requestDTO.getPassword2());
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        Role role = Role.valueOfRole(requestDTO.getRole());
        try {
            if (role == Role.COUPLE) {
                coupleJPARepository.save(requestDTO.toCoupleEntity());
            }
            else {
                plannerJPARepository.save(requestDTO.toPlannerEntity());
            }
        } catch (Exception e) {
            throw new ServerException(BaseException.USER_UNEXPECTED_ERROR);
        }
    }

    public String login(UserRequest.LoginDTO requestDTO) {
        User user = userJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new BadRequestException(BaseException.USER_EMAIL_NOT_FOUND + requestDTO.getEmail())
        );
        log.debug("디버그: 로그인 토큰 {}", user.getId());
        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException(BaseException.USER_PASSWORD_WRONG);
        }
        return jwtProvider.create(user);
    }

    public UserResponse.FindById findById(Long userId) {
        return new UserResponse.FindById(findUserById(userId));
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(Long userId) {
        findUserById(userId);
        userJPARepository.deleteById(userId);
    }

    private void sameCheckPassword(String password, String password2) {
        boolean isEqual = Objects.equals(password, password2);
        if (!isEqual){
            throw new BadRequestException(BaseException.USER_PASSWORD_NOT_SAME);
        }
    }

    private void sameCheckEmail(String email) {
        Optional<User> userOptional = userJPARepository.findByEmailNative(email);
        if (userOptional.isPresent()){
            throw new BadRequestException(BaseException.USER_EMAIL_EXIST);
        }
    }

    private User findUserById(Long userId){
        return userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }
}
