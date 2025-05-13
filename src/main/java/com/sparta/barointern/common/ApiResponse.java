package com.sparta.barointern.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    Process process;
    private T data;

    // 성공 응답을 위한 메서드
    public static <T> ApiResponse<T> success(T data) {

        return ApiResponse.<T>builder()
                .process(Process.success())
                .data(data)
                .build();
    }

    // 성공 응답을 위한 메시지 포함 메서드
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .process(Process.success(message))
                .data(data)
                .build();
    }

    // 실패 응답을 위한 메서드
    public static <T> ApiResponse<T> failure(String code, String message) {
        return ApiResponse.<T>builder()
                .process(Process.of(code, message))
                .data(null)
                .build();
    }

    // of 메서드를 사용하여 ApiResponseData 객체를 생성하는 메서드
    public static <T> ApiResponse<T> of(String code, String message, T data) {
        return ApiResponse.<T>builder()
                .process(Process.of(code, message))
                .data(data)
                .build();
    }
}
