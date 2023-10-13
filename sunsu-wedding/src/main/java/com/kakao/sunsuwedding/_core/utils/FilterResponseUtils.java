package com.kakao.sunsuwedding._core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterResponseUtils {

    private final ObjectMapper om;

    @Autowired
    public FilterResponseUtils(ObjectMapper om) {
        this.om = om;
    }

    public void unAuthorized(HttpServletResponse resp, UnauthorizedException e) throws IOException {
        resp.setStatus(e.status().value());
        resp.setContentType("application/json; charset=utf-8");
        String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
    }

    public void forbidden(HttpServletResponse resp, ForbiddenException e) throws IOException {
        resp.setStatus(e.status().value());
        resp.setContentType("application/json; charset=utf-8");
        String responseBody = om.writeValueAsString(e.body());
        resp.getWriter().println(responseBody);
    }
}
