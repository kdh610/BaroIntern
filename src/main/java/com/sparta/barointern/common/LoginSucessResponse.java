package com.sparta.barointern.common;

import lombok.Getter;

@Getter
public class LoginSucessResponse {
    private String token;

    public LoginSucessResponse(String token) {
        this.token = token;
    }
}
