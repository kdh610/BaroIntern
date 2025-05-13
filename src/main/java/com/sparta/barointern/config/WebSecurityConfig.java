package com.sparta.barointern.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 로그인 사용
        http.formLogin(Customizer.withDefaults());


        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login").permitAll() // resources 접근 허용 설정
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );


        return http.build();

    }

}
