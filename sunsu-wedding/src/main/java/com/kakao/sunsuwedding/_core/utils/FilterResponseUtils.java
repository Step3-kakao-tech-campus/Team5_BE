package com.kakao.sunsuwedding._core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterResponseUtils {

    private final ObjectMapper om;

    @Autowired
    public FilterResponseUtils(ObjectMapper om) {
        this.om = om;
    }

    private void responseSetting(HttpServletResponse resp, HttpStatusCode status, Object body) throws IOException {
        resp.setStatus(status.value());
        resp.setContentType("application/json; charset=utf-8");
        String responseBody = om.writeValueAsString(body);
        resp.getWriter().println(responseBody);
    }

    public void unAuthorized(HttpServletResponse resp, UnauthorizedException e) throws IOException {
        responseSetting(resp, e.status(), e.body());
    }

    public void forbidden(HttpServletResponse resp, ForbiddenException e) throws IOException {
        responseSetting(resp, e.status(), e.body());
    }

    public void notFound(HttpServletResponse resp, NotFoundException e) throws IOException {
        responseSetting(resp, e.status(), e.body());
    }
}
