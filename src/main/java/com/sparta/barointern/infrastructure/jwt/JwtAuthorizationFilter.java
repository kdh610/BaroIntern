package com.sparta.barointern.infrastructure.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.barointern.common.ErrorResponse;
import com.sparta.barointern.common.Process;
import com.sparta.barointern.infrastructure.exception.Code;
import com.sparta.barointern.application.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.getTokenFromRequest(request);
        log.info("[JWT 인가 Filter] token: {}", token);

        if(token != null) {
            if(!jwtUtil.validateToken(token)) {
                ResponseEntity<ErrorResponse> responseBody = ResponseEntity.badRequest()
                        .body(ErrorResponse.from(Process.from(Code.INVALID_TOKEN)));
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(responseBody.getBody()));
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                ResponseEntity<ErrorResponse> responseBody = ResponseEntity.badRequest()
                        .body(ErrorResponse.from(Process.from(Code.USER_NOT_FOUND)));
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(responseBody.getBody()));

                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
