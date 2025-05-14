package com.sparta.barointern.domain.entity;

import com.sparta.barointern.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public void grantAdmin() {
        this.userRole = UserRole.ADMIN;
    }


    public static User create(String username, String password, String nickname, UserRole userRole) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .userRole(userRole)
                .build();
    }

}
