package com.kakao.sunsuwedding._core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.errors.CustomException;
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

    public void writeResponse(HttpServletResponse response, CustomException exception) throws IOException {
        response.resetBuffer();
        response.setStatus(exception.status().value());
        response.setContentType("application/json; charset=utf-8");
        String responseBody = om.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }
}
