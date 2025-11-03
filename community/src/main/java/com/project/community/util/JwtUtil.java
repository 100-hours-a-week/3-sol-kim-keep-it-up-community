package com.project.community.util;

import com.project.community.dto.TokenResponseDto;
import com.project.community.entity.RefreshToken;
import com.project.community.entity.User;
import com.project.community.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60; // 15분
    private static final int REFRESH_TOKEN_EXPIRATION = 14 * 24 * 3600; // 14일

    private final RefreshTokenRepository refreshTokenRepository;

    private final Key key = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode("MjAyNeuFhDEx7JuUMuydvGp3dOq0gOugqOyGjOyKpOy9lOuTnOyekeyEsQ==")
    );

    /*
    토큰 생성
     */
    public TokenResponseDto generateAndSaveTokens(User user) {
        String accessToken = generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = generateRefreshToken(user.getId());

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setUserId(user.getId());
        refreshEntity.setToken(refreshToken);
        refreshEntity.setExpiresAt(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION));
        refreshEntity.setRevoked(false);
        refreshTokenRepository.save(refreshEntity);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public String generateAccessToken(Long userId, String email) {
        long accessTtlSec = 15 * 60;
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        long refreshTtlSec = 14L * 24 * 3600; // 14일 간 유효
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("typ", "refresh")
                .setId(UUID.randomUUID().toString()) // jti: 토큰 고유 식별자. 탈취, 중복 막기 위해 DB, 블랙리스트에서 재사용 여부를 추적할 때 활용
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(refreshTtlSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    파싱 및 유효성 검증
     */
    public Jws<Claims> parse(String jwt) {
        //JWS: Json Web Signature. JWT에 서명이 포함된 형태
        return Jwts
                .parserBuilder()
                .setSigningKey(key) // 서명 키를 사용해서
                .build() // 파서를 생성하고
                .parseClaimsJws(jwt); // 토큰의 서명과 만료 시간 확인
    }

    /*
    쿠키에 토큰 추가
     */
    public void addTokenCookies(HttpServletResponse response, TokenResponseDto tokenResponse, Integer maxAge) {
        addTokenCookie(response, "accessToken", tokenResponse.accessToken(), (maxAge != null) ? maxAge : ACCESS_TOKEN_EXPIRATION);
        addTokenCookie(response, "refreshToken", tokenResponse.refreshToken(), (maxAge != null) ? maxAge : REFRESH_TOKEN_EXPIRATION);
    }

    public void addTokenCookie(HttpServletResponse response, String name, String value, Integer maxAge) {
        if (maxAge == null && name.equals("accessToken")) {
            maxAge = ACCESS_TOKEN_EXPIRATION;
        } else if (maxAge == null && name.equals("refreshToken")) {
            maxAge = REFRESH_TOKEN_EXPIRATION;
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // javascript를 통한 쿠키 접근 제한
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }
}
