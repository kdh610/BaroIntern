package com.sparta.barointern.exception;

import com.sparta.barointern.common.ApiResponse;
import com.sparta.barointern.common.ErrorResponse;
import com.sparta.barointern.common.Process;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
