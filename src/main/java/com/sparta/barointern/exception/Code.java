package com.sparta.barointern.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {
    SUCCESS(HttpStatus.OK, "SUCCESS", "성공적으로 처리되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR","서버에러"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "잘못된 입력값이 존재합니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "ACCESS_DENIED_EXCEPTION", "권한이 없습니다."),


    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_NOT_FOUND","해당 유저가 없습니다."),
    USER_ALREADY_USERNAME_EXCEPTION(HttpStatus.BAD_REQUEST, "USER_ALREADY_USERNAME_EXCEPTION", "해당 유저네임이 이미 존재합니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED,"INVALID_CREDENTIALS","아이디 또는 비밀번호가 올바르지 않습니다.")
    ;

    private final HttpStatus status;
    private final String code;
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
