package com.kakao.sunsuwedding.user.token;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding.user.base_user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenServiceImpl implements TokenService {
    private final TokenJPARepository tokenJPARepository;
    private final JWTProvider jwtProvider;

    @Transactional
    public TokenDTO refreshAllTokens(User user) {
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        // security filter 에서 토큰 정보를 삭제하므로
        // 조회 과정 없이 엔티티 생성 후 바로 저장
        Token token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        try {
            tokenJPARepository.save(token);
        }
        catch (DataIntegrityViolationException exception) {
            throw new UnauthorizedException(BaseException.TOKEN_REFRESH_FORBIDDEN);
        }

        return new TokenDTO(accessToken, refreshToken);
    }

    @Transactional
    public void expireTokenByUserId(Long userId) {
        Token token = findTokenByUserId(userId);

        tokenJPARepository.delete(token);
    }

    public Boolean checkTokenPairValidation(Long userId, String accessToken, String refreshToken) {
        Token token = findTokenByUserId(userId);

        return token.getAccessToken().equals(accessToken) && token.getRefreshToken().equals(refreshToken);
    }

    private Token findTokenByUserId(Long userId) {
        return tokenJPARepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(BaseException.TOKEN_NOT_FOUND));
    }
}
