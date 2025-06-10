package com.sparta.barointern.infrastructure.jwt;


import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.infrastructure.exception.BaseException;
import com.sparta.barointern.infrastructure.exception.Code;
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
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final Long TOKEN_TIME = 60 * 1000L * 60 * 24 * 30;


    private SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secret){
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String createJwtToken(String username, Role role, String category) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(username)
                        .claim("category",category)
                        .claim("role",role.getRole().getRoleName())
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                        .signWith(secretKey)
                        .compact();
    }

    public boolean validateToken(String token) {
        log.info("validate token: {}", token);
        try {
            if (isExistToken(token)) {
                String actualToken = token.substring(BEARER_PREFIX.length());
                Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(actualToken);
                return true;
            }else{
                throw new BaseException(Code.INVALID_TOKEN);
            }
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("유효하지 않는 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
            throw new BaseException(Code.INVALID_TOKEN);
        }
    }

    private static boolean isExistToken(String token) {
        return token != null && token.startsWith(BEARER_PREFIX);
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

    public String getTokenFromRequest(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");

        if(authorization!=null && authorization.startsWith(BEARER_PREFIX)){
            return authorization;
        }
        return null;
    }


    public Claims getUserInfoFromToken(String token) {
        String actualToken = token.substring(BEARER_PREFIX.length());
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(actualToken).getPayload();
    }

    public String getRole(String token) {
        String actualToken = token.substring(BEARER_PREFIX.length());

        Map role = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(actualToken).getPayload().get("role", Map.class);
        return (String)role.get("role");

    }


}
