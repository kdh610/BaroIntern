package com.sparta.barointern.infrastructure.exception;

import com.sparta.barointern.common.ErrorResponse;
import com.sparta.barointern.common.Process;
import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.entity.User;
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

    // @Valid 유효성 검사에서 걸리는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        Process error = Process.of(Code.VALIDATION_ERROR.getCode(),Code.VALIDATION_ERROR.getMessage());
        // 반환할 메시지와 HTTP 상태 코드 설정
        return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(ErrorResponse.from(error));
    }



}
