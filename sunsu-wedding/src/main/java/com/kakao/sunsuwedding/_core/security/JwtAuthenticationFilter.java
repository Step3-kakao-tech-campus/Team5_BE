package com.kakao.sunsuwedding._core.security;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
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

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(JWTProvider.HEADER);

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }
        if (!jwt.startsWith("Bearer ")) {
            throw new JWTDecodeException("토큰 형식이 잘못되었습니다.");
        }
        try {
            DecodedJWT decodedJWT = JWTProvider.verify(jwt);
            int id = decodedJWT.getClaim("id").asInt();
            String roleName = decodedJWT.getClaim("role").asString();
            System.out.println("role : "+ roleName);

            Role role = Role.valueOfRole(roleName);

            if (role == Role.PLANNER){
                Planner planner = Planner.builder().id(id).build();
                CustomPlannerDetails myPlannerDetails = new CustomPlannerDetails(planner);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                myPlannerDetails,
                                myPlannerDetails.getPassword(),
                                myPlannerDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("디버그 : 예비 부부 인증 객체 만들어짐");
            }
            if (role == Role.COUPLE){
                Couple couple = Couple.builder().id(id).build();
                CustomCoupleDetails myCoupleDetails = new CustomCoupleDetails(couple);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                myCoupleDetails,
                                myCoupleDetails.getPassword(),
                                myCoupleDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("디버그 : 플래너 인증 객체 만들어짐");
            }
            else {
                log.error("role이 잘못됨");
            }

        } catch (SignatureVerificationException sve) {
            log.error("토큰 검증 실패");
        } catch (TokenExpiredException tee) {
            log.error("토큰 만료됨");
        } finally {
            chain.doFilter(request, response);
        }
    }
}
