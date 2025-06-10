package com.sparta.barointern;

import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.infrastructure.exception.BaseException;
import com.sparta.barointern.infrastructure.jwt.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc
class JwtUtilTests {

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("Jwt 토큰 생성")
	void createJwtToken(){
		String jwtToken = jwtUtil.createJwtToken("test", Role.create(UserRole.USER), "access");
		System.out.println(jwtToken);
		boolean result = jwtUtil.validateToken(jwtToken);
		Assertions.assertTrue(result);
	}

	@Test
	@DisplayName("Jwt 토큰 만료 검증")
	void validateExpiredJwtToken(){
		Assertions.assertThrows(BaseException.class,
				() -> jwtUtil.validateToken("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTQ0OTU3OSwiZXhwIjoxNzQ5NDQ5NTgwfQ.NNXwzhI49_H3LfbKYaapPedNAgVrLg8SQxNyraQrDYM"));
	}

	@Test
	@DisplayName("Jwt 토큰 없이 접근 검증")
	void validateNoJwtToken(){
		Assertions.assertThrows(BaseException.class,
				() -> jwtUtil.validateToken(null));
	}

	@Test
	@DisplayName("Jwt 잘못된 형식의 토큰 검증")
	void validateWrongJwtToken(){
		Assertions.assertThrows(BaseException.class,
				() -> jwtUtil.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTQ0OTU3OSwiZXhwIjoxNzQ5NDQ5NTgwfQ.NNXwzhI49_H3LfbKYaapPedNAgVrLg8SQxNyraQrDYM"));
	}




}