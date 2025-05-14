package com.sparta.barointern.application.dto.request;

import com.sparta.barointern.domain.entity.User;
import com.sparta.barointern.domain.enums.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSignupAppRequestDto {

    private String username;
    private String nickname;
    private String password;

    public User toEntity() {
        return User.create(this.username, this.password, this.nickname);
    }

    public static UserSignupAppRequestDto create(String username, String password, String nickname) {
        return UserSignupAppRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }

}
