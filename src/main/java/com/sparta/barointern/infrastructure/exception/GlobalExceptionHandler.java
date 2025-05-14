package com.sparta.barointern.infrastructure.exception;

import com.sparta.barointern.common.ErrorResponse;
import com.sparta.barointern.common.Process;
import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handlerBaseExceptionException(BaseException e) {
        Code code = e.getCode();
        Process error = Process.from(code);
        return ResponseEntity.status(code.getStatus()).body(ErrorResponse.from(error));
    }


}
