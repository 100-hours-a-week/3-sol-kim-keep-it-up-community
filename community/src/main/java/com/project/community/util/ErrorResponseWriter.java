package com.project.community.util;

import com.project.community.common.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ErrorResponseWriter {
    public static void writeError(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        String body = """
      { "code": "%s", "message": "%s" }
      """.formatted(code.name(), code.getErrorMessage());
        response.getWriter().write(body);
    }
}
