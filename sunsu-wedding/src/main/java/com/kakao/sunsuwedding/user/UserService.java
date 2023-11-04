package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.utils.DateFormat;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import com.kakao.sunsuwedding.user.token.Token;
import com.kakao.sunsuwedding.user.token.TokenDTO;
import com.kakao.sunsuwedding.user.token.TokenJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final TokenJPARepository tokenJPARepository;
    private final JWTProvider jwtProvider;

    @Transactional
    public UserResponse.FindUserId signup(UserRequest.SignUpDTO requestDTO) {
        sameCheckEmail(requestDTO.email());
        sameCheckPassword(requestDTO.password(), requestDTO.password2());
        Role role = Role.valueOfRole(requestDTO.role());
        String encodedPassword = passwordEncoder.encode(requestDTO.password());
        User user;
        if (role == Role.COUPLE) {
            user = coupleJPARepository.save(requestDTO.toCoupleEntity(encodedPassword));
        }
        else {
            user = plannerJPARepository.save(requestDTO.toPlannerEntity(encodedPassword));
        }
        return new UserResponse.FindUserId(user.getId());
    }

    @Transactional
    public Pair<TokenDTO, UserResponse.FindUserId> login(UserRequest.LoginDTO requestDTO) {
        User user = userJPARepository.findByEmail(requestDTO.email()).orElseThrow(
                () -> new BadRequestException(BaseException.USER_EMAIL_NOT_FOUND)
        );
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new BadRequestException(BaseException.USER_PASSWORD_WRONG);
        }

        Token token = tokenJPARepository.findByUserId(user.getId())
                .orElseGet(() -> Token.builder().user(user).build());

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        token.update(accessToken, refreshToken);

        tokenJPARepository.save(token);
        return Pair.of(
                new TokenDTO(token.getAccessToken(), token.getRefreshToken()),
                new UserResponse.FindUserId(user.getId()));
    }

    public UserResponse.FindById findById(User user) {
        User user1 = findUserById(user.getId());
        return UserDTOConverter.toFindByIdDTO(user1);
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(User user) {
        findUserById(user.getId());
        userJPARepository.deleteById(user.getId());
    }

    private void sameCheckPassword(String password, String password2) {
        Boolean isEqual = Objects.equals(password, password2);
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
