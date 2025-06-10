package com.sparta.barointern.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotBlank(message = "사용자 아이디는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 영문자와 숫자로만 구성된 4~20자리여야 합니다.")
    private String username;
    @NotBlank(message = "사용자 비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "비밀번호는 영문자와 숫자로만 구성된 4~20자리여야 합니다.")
    private String password;
}
