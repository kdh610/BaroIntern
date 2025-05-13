package com.sparta.barointern.Controller.dto.request;

import com.sparta.barointern.enums.UserRole;
import com.sparta.barointern.service.dto.request.UserSignupAppRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    private String username;
    private String nickname;
    private String password;
    private UserRole userRole = UserRole.USER;

    public UserSignupAppRequestDto toAppDto(){
        return UserSignupAppRequestDto.create(this.username, this.password, this.nickname, this.userRole);
    }

}
