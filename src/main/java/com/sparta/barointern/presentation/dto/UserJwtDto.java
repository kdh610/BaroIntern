package com.sparta.barointern.presentation.dto;

import com.sparta.barointern.domain.enums.UserRole;
import lombok.Getter;

@Getter
public class UserJwtDto {
    private String username;
    private UserRole role;

}
