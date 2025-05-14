package com.sparta.barointern.application.dto.response;

import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.entity.User;
import com.sparta.barointern.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAppResponseDto {
    
    private String username;
    private String nickname;
    private List<Role> roles;

    public static UserAppResponseDto from(User user) {
        return UserAppResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .roles(user.getRoles())
                .build();
    }
}
