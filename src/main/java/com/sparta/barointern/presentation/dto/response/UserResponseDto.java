package com.sparta.barointern.presentation.dto.response;

import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    
    private String username;
    private String nickname;
    private List<Role> roles;

    public static UserResponseDto from(UserAppResponseDto userAppResponseDto){
        return UserResponseDto.builder()
                .username(userAppResponseDto.getUsername())
                .nickname(userAppResponseDto.getNickname())
                .roles(userAppResponseDto.getRoles())
                .build();
    }

}
