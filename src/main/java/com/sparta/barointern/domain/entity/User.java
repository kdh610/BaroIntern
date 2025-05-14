package com.sparta.barointern.domain.entity;

import com.sparta.barointern.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    private String username;
    private String nickname;
    private String password;
    @Builder.Default
    private List<Role> roles= new ArrayList<>(List.of(Role.create(UserRole.USER)));

    public void grantAdmin() {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        if (!this.roles.contains(UserRole.ADMIN)) {
            this.roles.set(0,Role.create(UserRole.ADMIN));
        }
    }

    public static User create(String username, String password, String nickname) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .build();
    }

}
