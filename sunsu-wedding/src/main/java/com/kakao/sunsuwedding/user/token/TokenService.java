package com.kakao.sunsuwedding.user.token;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding.user.base_user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenJPARepository tokenJPARepository;
    private final JWTProvider jwtProvider;

    public TokenDTO refreshAllTokens(User user) {
        Token token = tokenJPARepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException(BaseException.TOKEN_NOT_FOUND.getMessage()));

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        token.update(accessToken, refreshToken);
        tokenJPARepository.save(token);

        return new TokenDTO(accessToken, refreshToken);
    }

    public void expireAllTokenByUserId(Long userId) {
        Token token = tokenJPARepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(BaseException.TOKEN_NOT_FOUND.getMessage()));

        tokenJPARepository.delete(token);
    }

    public Boolean checkTokenValidation(Long userId, String accessToken, String refreshToken) {
        Token token = tokenJPARepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(BaseException.TOKEN_NOT_FOUND.getMessage()));

        return token.getAccessToken().equals(accessToken) && token.getRefreshToken().equals(refreshToken);
    }
}
