package com.sparta.barointern.common.jwt;

import com.sparta.barointern.dto.UserJwtDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 1000L * 60 * 24 * 7;

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

    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }



}
