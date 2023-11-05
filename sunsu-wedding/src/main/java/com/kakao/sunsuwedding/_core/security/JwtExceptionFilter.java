package com.kakao.sunsuwedding._core.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.TokenException;
import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import com.kakao.sunsuwedding._core.utils.FilterResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final FilterResponseUtils filterResponseUtils;

    @Autowired
    public JwtExceptionFilter(FilterResponseUtils filterResponseUtils) {
        this.filterResponseUtils = filterResponseUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        }
        catch (TokenException | UnauthorizedException | ForbiddenException | NotFoundException exception) {
            filterResponseUtils.writeResponse(response, exception);
        }
        catch (JWTCreationException | JWTVerificationException e) {
            filterResponseUtils.writeResponse(response, new UnauthorizedException(BaseException.TOKEN_NOT_FOUND));
        }
    }
}