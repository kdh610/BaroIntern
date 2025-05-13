package com.sparta.barointern.util.jwt;

import com.sparta.barointern.dto.UserJwtDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final Long TOKEN_TIME = 60 * 1000L * 60 * 24 * 7;

    private SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secret){
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String createJwtToken(UserJwtDto userJwtDto, String category) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(userJwtDto.getUsername())
                        .claim("category",category)
                        .claim("role",userJwtDto.getRole())
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                        .signWith(secretKey)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            if (token != null && token.startsWith(BEARER_PREFIX)) {
                String actualToken = token.substring(BEARER_PREFIX.length());
                log.info("JWTUTIL token: {}", actualToken);
                Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(actualToken);
                return true;
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Cookie createCookie(String token){
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
        cookie.setMaxAge(TOKEN_TIME.intValue());
        cookie.setPath("/");

        return cookie;
    }




}
