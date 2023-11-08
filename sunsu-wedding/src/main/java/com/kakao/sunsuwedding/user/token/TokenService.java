package com.kakao.sunsuwedding.user.token;

import com.kakao.sunsuwedding.user.base_user.User;

public interface TokenService {

    TokenDTO refreshAllTokens(User user);

    void expireTokenByUserId(Long userId);

    Boolean checkTokenValidation(Long userId, String accessToken, String refreshToken);
}
