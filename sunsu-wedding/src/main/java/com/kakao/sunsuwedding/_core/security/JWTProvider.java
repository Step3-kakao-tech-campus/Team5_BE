package com.kakao.sunsuwedding._core.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.sunsuwedding.user.base_user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTProvider {
    // access-token expire time = 2 min
    public final Long ACCESS_TOKEN_EXP = 1000L * 60 * 2;
    // refresh-token expire time = 3 min
    public final Long REFRESH_TOKEN_EXP = 1000L * 60 * 3;
    public final String TOKEN_PREFIX = "Bearer ";
    public final String AUTHORIZATION_HEADER = "Authorization";
    public final String REFRESH_HEADER = "Refresh";

    @Value("${security.jwt-config.secret.access}")
    public String ACCESS_TOKEN_SECRET;

    @Value("${security.jwt-config.secret.refresh}")
    private String REFRESH_TOKEN_SECRET;

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
