package com.sparta.barointern.entity;

import com.sparta.barointern.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "p_user")
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


    public static User create(String username, String password, String nickname, UserRole userRole) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .userRole(userRole)
                .build();
    }

}
