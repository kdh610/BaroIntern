package com.sparta.barointern.domain.entity;

import com.sparta.barointern.domain.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Role {

    private UserRole role;

    public static Role create(UserRole userRole){
        return Role.builder()
                .role(userRole)
                .build();
    }

}
