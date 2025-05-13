package com.sparta.barointern.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // Form 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());


        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/signup").permitAll() // resources 접근 허용 설정
                .requestMatchers("/login").permitAll() // resources 접근 허용 설정
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리

        );


        return http.build();

    }

}
