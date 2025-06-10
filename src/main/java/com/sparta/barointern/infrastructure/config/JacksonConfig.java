package com.sparta.barointern.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary // Spring이 여러 ObjectMapper Bean을 발견했을 때, 이것을 기본으로 사용하도록 강제합니다.
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // [핵심] Jackson 2.12+ 버전부터 도입된 더 세분화된 타입 변환 설정
        // 이 설정은 '정수(Integer) 형태의 입력을 텍스트(Textual, 즉 String) 타입으로 변환하려고 할 때,
        // 변환을 시도하지 말고 실패(Fail)시켜라' 라는 매우 구체적인 규칙입니다.
        mapper.coercionConfigFor(LogicalType.Textual)
                .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail);

        // 만약 boolean 타입도 문자열로 변환되는 것을 막고 싶다면 아래 설정도 추가
        // mapper.coercionConfigFor(LogicalType.Textual)
        //       .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);

        return mapper;
    }
}
