package com.kakao.sunsuwedding._core.security;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.sunsuwedding._core.errors.exception.TokenException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.token.ErrorStatus;
import com.kakao.sunsuwedding.user.token.TokenService;
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
    private final TokenService tokenService;

    private final JWTProvider jwtProvider;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTProvider jwtProvider, TokenService tokenService) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
    }

    // transcation 을 열어야 할 것 같은데
    // 접근 제한자를 protected 에서 public 으로 바꿔야 합니다...!!
    // 바꿔도 괜찮을까요 ..???
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(jwtProvider.AUTHORIZATION_HEADER);
        String refreshToken = request.getHeader(jwtProvider.REFRESH_HEADER);

        // access token 이 없을 수는 없다.
        // refresh token 만으로는 인증 불가
        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        // access token, refresh token 둘 다 있을 경우
        // access token 이 만료되어 refresh token 까지 헤더에 담아 인증 요청을 진행하는 경우이므로
        // access token 은 유효하지 않은 토큰이어야 한다.
        // 그리고 데이터베이스에 저장된 refresh token 과 access token 쌍이랑
        // 요청으로 들어온 두 토큰 쌍이 일치하면
        // 새로운 refresh token, access token 을 발급해준다.
        if (refreshToken != null) {
            try {
                // access token 유효성 검증
                Boolean isAccessTokenValid = jwtProvider.isValidAccessToken(accessToken);

                // access token 이 유효한 상태에서 refresh 요청이 들어온 경우이다.
                // access token 을 decode 할 수 있는 경우이므로
                // decode 해서 나온 유저의 id 값을 통해 토큰 저장소에서 토큰 검색
                // 해당 토큰을 전부 폐기 처리
                if (isAccessTokenValid) {
                    Long userId = jwtProvider
                            .verifyAccessToken(accessToken)
                            .getClaim("id")
                            .asLong();
                    tokenService.expireTokenByUserId(userId);
                    throw new TokenException(ErrorStatus.ACCESS_TOKEN_ALIVE);
                }

                // 여기에 도달하면 access token 은 만료된 상태에서 refresh 토큰은 존재하는 상태
                // refresh 토큰과 access token 쌍을 저장소의 쌍과 일치하면
                // 인증 객체 생성
                DecodedJWT decodedJWT = jwtProvider.verifyRefreshToken(refreshToken);
                Long userId = decodedJWT.getClaim("id").asLong();
                Boolean isTokenPairValid = tokenService.checkTokenValidation(userId, accessToken, refreshToken);

                // 토큰 쌍이 유효하다면 인증 객체 생성
                // 이 경우는 앞서 설명한 것처럼 refresh 목적으로 들어온 요청이므로
                // 서버에 저장된 기존 토큰은 무조건 삭제되어야 한다.
                tokenService.expireTokenByUserId(userId);

                // 서버에 저장된 토큰과 일치하면 인증 객체 생성
                if (isTokenPairValid) {
                    createAuthentication(decodedJWT, userId);
                    log.debug("refresh-token 을 이용한 인증 객체 생성");
                }
                // 서버에 저장된 토큰 쌍과 일치하지 않으면 오류 반환
                else {
                    throw new TokenException(ErrorStatus.INVALID_TOKEN);
                }
            } catch (SignatureVerificationException sve) {
                log.error("refresh token 검증 실패");
            } catch (TokenExpiredException tee) {
                log.error("refresh token 만료됨");
                // access, refresh token 둘 다 만료 시에는 401 에러 전달
                throw new TokenException(ErrorStatus.ALL_TOKEN_EXPIRED);
            } catch (JWTDecodeException jde) {
                log.error("잘못된 refresh token");
            } finally {
                chain.doFilter(request, response);
            }
        }

        // access token 만 있다면
        // 기존과 동일하게 로직 처리하고
        // TokenExpiredException 이 발생하면 403 에러 반환
        else {
            try {
                DecodedJWT decodedJWT = jwtProvider.verifyAccessToken(accessToken);
                Long userId = decodedJWT.getClaim("id").asLong();

                createAuthentication(decodedJWT, userId);
                log.debug("access-token 을 이용한 인증 객체 생성");
            } catch (SignatureVerificationException sve) {
                log.error("access token 검증 실패");
            } catch (TokenExpiredException tee) {
                log.error("access token 만료됨");
                // access token 만 만료 시에는 403 에러 전달
                throw new TokenException(ErrorStatus.EXPIRED_TOKEN);
            } catch (JWTDecodeException jde) {
                log.error("잘못된 access token");
            } catch (Exception e){
                log.error("access token 예상치 못한 에러");
            } finally {
                chain.doFilter(request, response);
            }
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
