package com.sparta.barointern.presentation.dto.request;

import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.application.dto.request.UserSignupAppRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "사용자 아이디는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9 ]{1,20}$", message = "아이디는 영문자와 숫자로만 구성된 4~20자리여야 합니다.")
    private String username;
    @NotBlank(message = "사용자 닉네임은 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "닉네임은 영문자와 숫자로만 구성된 4~20자리여야 합니다.")
    private String nickname;
    @NotBlank(message = "사용자 비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "비밀번호는 영문자와 숫자로만 구성된 4~20자리여야 합니다.")
    private String password;

    public UserSignupAppRequestDto toAppDto(){
        return UserSignupAppRequestDto.create(this.username, this.password, this.nickname);
    }

}
