package com.sparta.barointern.infrastructure.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.barointern.common.ErrorResponse;
import com.sparta.barointern.common.Process;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GlobalExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException ex) {
            handleBuiltInException(response);
        }
    }

    private void handleBuiltInException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseEntity<ErrorResponse> responseBody = ResponseEntity.badRequest()
                .body(ErrorResponse.from(Process.from(Code.INVALID_TOKEN)));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody.getBody()));
    }

}
