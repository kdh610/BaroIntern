package com.sparta.barointern.common;

import com.sparta.barointern.infrastructure.exception.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Process {
    private String code;
    private String message;

    public static Process from(Code code) {
        return Process.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();
    }

    public static Process success() {
        return Process.builder()
                .code(Code.SUCCESS.getCode())
                .message(Code.SUCCESS.getMessage())
                .build();
    }

    public static Process success(String message) {
        return Process.builder()
                .code(Code.SUCCESS.getCode())
                .message(message)
                .build();
    }

    public static Process of(String code, String message) {
        return Process.builder()
                .code(code)
                .message(message)
                .build();
    }




}
