package com.sparta.barointern.service.dto.request;

import com.sparta.barointern.Controller.dto.request.UserSignupRequestDto;
import com.sparta.barointern.entity.User;
import com.sparta.barointern.enums.UserRole;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSignupAppRequestDto {



    private String username;
    private String nickname;
    private String password;
    private UserRole userRole = UserRole.USER;

    public User toEntity() {
        return User.create(this.username, this.password, this.nickname, this.userRole);
    }

    public static UserSignupAppRequestDto create(String username, String password, String nickname, UserRole userRole) {
        return UserSignupAppRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .userRole(userRole).build();
    }

}
