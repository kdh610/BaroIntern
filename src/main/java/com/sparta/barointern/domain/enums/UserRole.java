package com.sparta.barointern.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER("USER"),
    HUB_MANAGER("ADMIN");


    private final String roleName;
}
