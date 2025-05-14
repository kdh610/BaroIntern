package com.sparta.barointern.infrastructure.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.barointern.common.ApiResponse;
import com.sparta.barointern.common.ErrorResponse;
import com.sparta.barointern.common.LoginSucessResponse;
import com.sparta.barointern.common.Process;
import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.exception.BaseException;
import com.sparta.barointern.exception.Code;
import com.sparta.barointern.infrastructure.security.UserDetailsImpl;
import com.sparta.barointern.presentation.dto.request.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            log.info("[Jwt 인증 Filter] Attempting to authenticate user: " + requestDto.getUsername());
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(), null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("[Jwt 인증 Filter] Successfully authenticated user: " + authResult.getPrincipal());
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRole userRole = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUserRole();

        String token = jwtUtil.createJwtToken(username, userRole, "access");
        Cookie cookie = jwtUtil.createCookie(token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addCookie(cookie);
        ResponseEntity<LoginSucessResponse> responseBody = ResponseEntity.ok(new LoginSucessResponse(token.substring(7)));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody.getBody()));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("[Jwt 인증 Filter] Unsuccessful authentication");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseEntity<ErrorResponse> responseBody = ResponseEntity.badRequest()
                .body(ErrorResponse.from(Process.from(Code.INVALID_CREDENTIALS)));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody.getBody()));
    }
}
