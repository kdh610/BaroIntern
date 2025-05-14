package com.sparta.barointern.domain.entity;

import com.sparta.barointern.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {



    private String username;
    private String nickname;
    private String password;
    @Builder.Default
    private List<Role> roles= new ArrayList<>(List.of(Role.create(UserRole.USER)));

    public void grantAdmin() {
        if (this.roles == null) { // 혹시 roles가 초기화 안됐을 수도 있으니 방어 코드
            this.roles = new ArrayList<>();
        }
        // 이미 ADMIN 역할이 있는지 확인하고 없으면 추가
        if (!this.roles.contains(UserRole.ADMIN)) {
            this.roles.set(0,Role.create(UserRole.ADMIN));
        }
    }


    public static User create(String username, String password, String nickname) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
//                .roles(roles)
                .build();
    }

}
