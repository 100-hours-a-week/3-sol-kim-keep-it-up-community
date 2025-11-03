package com.project.community.filter;

import com.project.community.common.ErrorCode;
import com.project.community.util.ErrorResponseWriter;
import com.project.community.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ErrorResponseWriter errorWriter;

    private static final String[] EXCLUDED_PATHS = {
            "/**/signUp", "/**/signIn", "/**/list", "/**/detail/*", "/**/viewcount", "/**/refresh",
            "/images/**", "/legal/**"
    };

    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(EXCLUDED_PATHS).anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {

        Optional<String> token = extractToken(request);
        if (token.isEmpty() || token.get().isBlank()) {
            errorWriter.writeError(response, ErrorCode.SIGNIN_NEEDED);
            return;
        }

        if (!validateAndSetAttributes(token.get(), request)) {
            errorWriter.writeError(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        chain.doFilter(request, response);
    }

    /*
    토큰 추출
     */
    private Optional<String> extractToken(HttpServletRequest request) {
        return extractTokenFromHeader(request)
                .or(() -> extractTokenFromCookie(request));
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }

    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    /*
    토큰 유효성 검증 및 속성 값 할당
     */
    private boolean validateAndSetAttributes(String token, HttpServletRequest request) {
        try {
            var jws = jwtUtil.parse(token);
            Claims body = jws.getBody();
            request.setAttribute("userId", Long.valueOf(body.getSubject()));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
