package com.sparta.barointern.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Process error;

    public static ErrorResponse from(Process error) {
        return ErrorResponse.builder()
                .error(error)
                .build();
    }



}
