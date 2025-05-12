package com.sparta.barointern.dto;

import com.sparta.barointern.enums.UserRole;
import lombok.Getter;

@Getter
public class UserJwtDto {
    private String username;
    private UserRole role;

}
