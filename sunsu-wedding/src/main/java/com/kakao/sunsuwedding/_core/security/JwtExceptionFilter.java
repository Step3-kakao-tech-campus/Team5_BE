package com.kakao.sunsuwedding._core.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.errors.exception.Exception401;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JWTCreationException | JWTVerificationException e) {
            setResponse(response, new Exception401("잘못된 토큰입니다."));
        }
    }

    private void setResponse(HttpServletResponse response, Exception401 e) throws RuntimeException, IOException {
        response.setStatus(e.status().value());
        response.setContentType("application/json; charset=utf-8");
        response.getOutputStream().write(om.writeValueAsBytes(e.body()));
    }
}