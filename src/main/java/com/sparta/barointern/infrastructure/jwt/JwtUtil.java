package com.sparta.barointern.infrastructure.jwt;


import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.exception.BaseException;
import com.sparta.barointern.exception.Code;
import com.sparta.barointern.presentation.dto.UserJwtDto;
import io.jsonwebtoken.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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


    public String createJwtToken(String username, Role role, String category) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(username)
                        .claim("category",category)
                        .claim("role",role)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                        .signWith(secretKey)
                        .compact();
    }

    public boolean validateToken(String token) {
        log.info("validate token: {}", token);
        boolean b = token.startsWith(BEARER_PREFIX);
        try {
            if (token != null && token.startsWith(BEARER_PREFIX)) {
                String actualToken = token.substring(BEARER_PREFIX.length());
                log.info("JWTUTIL token: {}", actualToken);
                Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(actualToken);
                return true;
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("원되지 않는 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
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

    public String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(AUTHORIZATION_HEADER)){
                    try{
                        return URLDecoder.decode(cookie.getValue(),"UTF-8");
                    }catch (UnsupportedEncodingException e){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public Claims getUserInfoFromToken(String token) {
        String actualToken = token.substring(BEARER_PREFIX.length());
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(actualToken).getBody();
    }

    public String getRole(String token) {
        String actualToken = token.substring(BEARER_PREFIX.length());
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(actualToken).getPayload().get("role", String.class);
    }


}
