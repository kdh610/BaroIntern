package com.sparta.barointern.presentation.dto.response;

import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    
    private String username;
    private String nickname;
    private UserRole userRole = UserRole.USER;

    public static UserResponseDto from(UserAppResponseDto userAppResponseDto){
        return UserResponseDto.builder()
                .username(userAppResponseDto.getUsername())
                .nickname(userAppResponseDto.getNickname())
                .userRole(userAppResponseDto.getUserRole())
                .build();
    }

}
