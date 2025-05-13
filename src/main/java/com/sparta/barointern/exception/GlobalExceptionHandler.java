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

    // @Valid 유효성 검사에서 걸리는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        // 반환할 메시지와 HTTP 상태 코드 설정
        return ResponseEntity.status(Code.VALIDATION_ERROR.getStatus()).body(ApiResponse.of(Code.VALIDATION_ERROR.getCode(),
                ex.getBindingResult().getFieldError().getDefaultMessage(),null));
    }
}
