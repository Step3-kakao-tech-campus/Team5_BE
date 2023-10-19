package com.kakao.sunsuwedding._core.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.sunsuwedding.user.base_user.User;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTProvider {
    // access-token expire time = 30 min
    public final Long ACCESS_TOKEN_EXP = 1000L * 60 * 30;
    // refresh-token expire time = 2 week
    public final Long REFRESH_TOKEN_EXP = 1000L * 60 * 60 * 24 * 14;
    public final String TOKEN_PREFIX = "Bearer ";
    public final String AUTHORIZATION_HEADER = "Authorization";
    public final String REFRESH_HEADER = "Refresh";

    // 테스트용 secret. 이후 환경변수 파일 분리시켜야 합니다 !!!
    public final String ACCESS_TOKEN_SECRET = "hjxgPJUzzHL7Uy3wWBbFdbTOn1qKZXAlbsl8XqSv3MQw9uV8dy73cp5lkgJZmSay0BnffwjBNRAttLVoy1Fqtg==";
    public final String REFRESH_TOKEN_SECRET = "/pNWcqH3BGCSyooP+vjaMPm+gPmsJByaeQ55mOi00ZGGqFWPJ2NUmEkkWHwl1mye988UXF7TgvvHZg+vEwlYwg==";

    public String createAccessToken(User user) {
        String jwt = create(user, ACCESS_TOKEN_EXP, ACCESS_TOKEN_SECRET);
        return TOKEN_PREFIX + jwt;
    }

    public String createRefreshToken(User user) {
        String jwt = create(user, REFRESH_TOKEN_EXP, REFRESH_TOKEN_SECRET);
        return TOKEN_PREFIX + jwt;
    }

    private String create(User user, Long expire, String secret) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + expire))
                .withClaim("id", user.getId())
                .withClaim("role", user.getDtype())
                .sign(Algorithm.HMAC512(secret));
    }

    public DecodedJWT verifyAccessToken(String token) throws SignatureVerificationException, TokenExpiredException {
        return verify(token, ACCESS_TOKEN_SECRET);
    }

    public DecodedJWT verifyRefreshToken(String token) throws SignatureVerificationException, TokenExpiredException {
        return verify(token, REFRESH_TOKEN_SECRET);
    }

    private DecodedJWT verify(String token, String secret) {
        token = token.replace(TOKEN_PREFIX, "");
        return JWT
                .require(Algorithm.HMAC512(secret))
                .build()
                .verify(token);
    }

    public boolean isValidAccessToken(String token) {
        try {
            verify(token, ACCESS_TOKEN_SECRET);
            return true;
        }
        catch (JWTVerificationException exception) {
            return false;
        }
    }
}
