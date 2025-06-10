package com.sparta.barointern.infrastructure.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. API 정보를 설정합니다.
        Info info = new Info()
                .title("BaroIntern API 명세서")
                .version("v1.0.0")
                .description("프로젝트의 API에 대한 문서입니다.");

        String jwtSchemeName = "bearerAuth";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName) // 스킴 이름
                        .type(SecurityScheme.Type.HTTP) // 인증 타입: HTTP
                        .scheme("bearer") // 스킴: bearer (JWT)
                        .bearerFormat("JWT")); // 베어러 토큰 형식

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }


}
