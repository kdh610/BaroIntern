package com.sparta.barointern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.barointern.presentation.dto.request.UserSignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BarointernApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("회원가입 정상 입력")
	void signUpSucesss() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("testuser")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect((ResultMatcher) status().isOk())
				.andExpect(jsonPath("$.username").value("testuser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
		;

		// 중복 회원가입
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect((ResultMatcher) status().isBadRequest())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXIST"))
				.andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."))

		;


		// 관리자 회원가입
		UserSignupRequestDto reqestAdmin = UserSignupRequestDto.builder()
				.username("testAdmin")
				.password("password")
				.nickname("admin")
				.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/signup/admin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqestAdmin)))
				.andExpect((ResultMatcher) status().isOk())
				.andExpect(jsonPath("$.username").value("testAdmin"))
				.andExpect(jsonPath("$.nickname").value("admin"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"))
		;
	}
