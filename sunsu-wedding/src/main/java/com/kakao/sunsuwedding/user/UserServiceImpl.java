package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.utils.UserDataChecker;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.email.EmailCode;
import com.kakao.sunsuwedding.user.email.EmailCodeJPARepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final TokenJPARepository tokenJPARepository;
    private final EmailCodeJPARepository emailCodeJPARepository;
    private final JWTProvider jwtProvider;
    private final UserDataChecker userDataChecker;

    @Transactional
    public UserResponse.FindUserId signup(UserRequest.SignUpDTO requestDTO) {

        userDataChecker.checkEmailAlreadyExist(requestDTO.email());
        userDataChecker.checkPasswordIsSame(requestDTO.password(), requestDTO.password2());
        userDataChecker.checkEmailAuthenticated(requestDTO);

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

        User user = findUserByRequest(requestDTO);
        userDataChecker.verifyPassword(requestDTO, user);

        Token token = findTokenByUser(user);

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        token.update(accessToken, refreshToken);

        tokenJPARepository.save(token);
        return Pair.of(
                new TokenDTO(token.getAccessToken(), token.getRefreshToken()),
                new UserResponse.FindUserId(user.getId()));
    }

    public UserResponse.FindById findById(Long userId) {
        User user1 = findUserById(userId);
        return UserDTOConverter.toFindByIdDTO(user1);
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(User user) {
        User user1 = findUserById(user.getId());

        // 탈퇴 시 이메일 인증 정보 삭제
        EmailCode emailCode = findEmailCodeByUser(user1);
        emailCodeJPARepository.delete(emailCode);

        userJPARepository.deleteById(user.getId());
    }

    private User findUserById(Long userId){
        return userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }

    private User findUserByRequest(UserRequest.LoginDTO requestDTO) {
        return userJPARepository.findByEmail(requestDTO.email()).orElseThrow(
                () -> new BadRequestException(BaseException.USER_EMAIL_NOT_FOUND)
        );
    }

    private Token findTokenByUser(User user) {
        return tokenJPARepository.findByUserId(user.getId())
                .orElseGet(() -> Token.builder().user(user).build());
    }

    private EmailCode findEmailCodeByUser(User user) {
        return emailCodeJPARepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException(BaseException.USER_EMAIL_NOT_FOUND));
    }
}
