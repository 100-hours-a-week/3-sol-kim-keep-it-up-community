package com.project.community.config;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // getting existing session (do not create a new one even though nothing exists)
        HttpSession session = request.getSession(false);

        // throw an error when the authentication failed
        if (session == null || session.getAttribute("userId") == null) {
            throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        }

        // allowing the request to proceed
        return true;
    }
}