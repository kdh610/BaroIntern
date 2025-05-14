package com.sparta.barointern.infrastructure.exception;

import lombok.Getter;

import static com.sparta.barointern.infrastructure.exception.Code.INTERNAL_SERVER_ERROR;


@Getter
public class BaseException extends RuntimeException {
    private final Code code;

    public BaseException(String message) {
        super(message);
        this.code = INTERNAL_SERVER_ERROR;
    }

    // 에러 코드를 지정하는 생성자
    public BaseException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    // 에러 코드와 메시지를 받는 생성자
    public BaseException(Code code, String message) {
        super(message);
        this.code = code;
    }
}
