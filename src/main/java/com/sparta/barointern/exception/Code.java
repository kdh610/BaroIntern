package com.sparta.barointern.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {
    SUCCESS(HttpStatus.OK, 0, "성공적으로 처리되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,1,"서버에러"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 2, "잘못된 입력값이 존재합니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, 3, "권한이 없습니다."),

    ;

    private final HttpStatus status;
    private final Integer code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}
