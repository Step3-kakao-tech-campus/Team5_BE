package com.kakao.sunsuwedding._core.security;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.token.TokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final TokenServiceImpl tokenServiceImpl;

    private final JWTProvider jwtProvider;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTProvider jwtProvider, TokenServiceImpl tokenServiceImpl) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.tokenServiceImpl = tokenServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(jwtProvider.AUTHORIZATION_HEADER);
        String refreshToken = request.getHeader(jwtProvider.REFRESH_HEADER);

        if (request.getRequestURI().contains("/api/portfolio") && request.getMethod().equals("GET")) {
            if (accessToken != null && !jwtProvider.isValidAccessToken(accessToken)) {
                throw new UnauthorizedException(BaseException.ACCESS_TOKEN_EXPIRED);
            }
        }

        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        if (refreshToken != null) {
            try {
                refreshTokenAndCreateAuthentication(accessToken, refreshToken);
            } catch (SignatureVerificationException sve) {
                log.error("refresh token 검증 실패");
            } catch (TokenExpiredException tee) {
                log.error("refresh token 만료됨");
                throw new UnauthorizedException(BaseException.ALL_TOKEN_EXPIRED);
            } catch (JWTDecodeException jde) {
                log.error("잘못된 refresh token");
            } finally {
                chain.doFilter(request, response);
            }
        }
        else {
            try {
                if (request.getRequestURI().equals("/api/user/token")) {
                    throw new UnauthorizedException(BaseException.REFRESH_TOKEN_REQUIRED);
                }
                DecodedJWT decodedJWT = jwtProvider.verifyAccessToken(accessToken);
                Long userId = decodedJWT.getClaim("id").asLong();
                createAuthentication(decodedJWT, userId);
                log.debug("access-token 을 이용한 인증 객체 생성");
            } catch (SignatureVerificationException sve) {
                log.error("access token 검증 실패");
            } catch (TokenExpiredException tee) {
                log.error("access token 만료됨");
                throw new UnauthorizedException(BaseException.ACCESS_TOKEN_EXPIRED);
            } catch (JWTDecodeException jde) {
                log.error("잘못된 access token");
            } finally {
                chain.doFilter(request, response);
            }
        }
    }

    private void refreshTokenAndCreateAuthentication(String accessToken, String refreshToken) {
        Boolean isAccessTokenValid = jwtProvider.isValidAccessToken(accessToken);

        if (isAccessTokenValid) {
            throw new UnauthorizedException(BaseException.ACCESS_TOKEN_STILL_ALIVE);
        }

        DecodedJWT decodedJWT = jwtProvider.verifyRefreshToken(refreshToken);
        Long userId = decodedJWT.getClaim("id").asLong();
        Boolean isTokenPairValid = tokenServiceImpl.checkTokenPairValidation(userId, accessToken, refreshToken);

        if (isTokenPairValid) {
            tokenServiceImpl.expireTokenByUserId(userId);
            createAuthentication(decodedJWT, userId);
            log.debug("refresh-token 을 이용한 인증 객체 생성");
        }
        else {
            throw new UnauthorizedException(BaseException.TOKEN_NOT_VALID);
        }
    }

    private void createAuthentication(DecodedJWT decodedJWT, Long userId) {
        String roleName = decodedJWT.getClaim("role").asString();
        Role role = Role.valueOfRole(roleName);
        User user = getUser(userId, role);
        CustomUserDetails myUserDetails = new CustomUserDetails(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        myUserDetails,
                        myUserDetails.getPassword(),
                        myUserDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static User getUser(Long userId, Role role) {
        return (role == Role.PLANNER) ? Planner.builder().id(userId).build() : Couple.builder().id(userId).build();
    }
}
