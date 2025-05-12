package com.sparta.barointern.Controller;

import com.sparta.barointern.common.jwt.JwtUtil;
import com.sparta.barointern.dto.UserJwtDto;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtUtil jwtUtil;


    @GetMapping("/jwt")
    public String jwt(HttpServletResponse res, UserJwtDto userJwtDto) {
        log.info("userJwtDto: {}" , userJwtDto);
        String accessToken = jwtUtil.createJwtToken(userJwtDto, "access");
        log.info("accessToken: {}", accessToken);
        Cookie cookie = jwtUtil.createCookie(accessToken);
        log.info("cookie: {}", cookie.toString());
        res.addCookie(cookie);
        return accessToken;
    }

    @GetMapping("/check/jwt")
    public boolean checkJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String token) {
        log.info("token: {}", token);
        Boolean isvalid = jwtUtil.validateToken(token);

        return isvalid;
    }

}
