package com.sparta.barointern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.barointern.domain.repository.UserRepository;
import com.sparta.barointern.infrastructure.jwt.JwtUtil;
import com.sparta.barointern.presentation.dto.request.UserSignupRequestDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BarointernApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;


	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}


	@Test
	@DisplayName("일반 회원가입 성공")
	void normalUserSignUp() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("signUp")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("signUp"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
		;
	}

	@Test
	@DisplayName("회원가입 nickname 미입력 validation 실패")
	void invalidNicknameSignUp() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("signUp")
				.password("password")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.error.message").value("잘못된 입력값이 존재합니다."));
		;
	}

	@Test
	@DisplayName("회원가입 username validation 실패")
	void invalidUsernameSignUp() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.error.message").value("잘못된 입력값이 존재합니다."));
		;
	}

	@Test
	@DisplayName("회원가입 password validation 실패")
	void invalidPwSignUp() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("test")
				.password("")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.error.message").value("잘못된 입력값이 존재합니다."));
		;
	}


	@Test
	@DisplayName("중복회원가입")
	void signUp() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("signUp")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("signUp"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
		;

		// 중복 회원가입
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
				.andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."))
		;
	}


	@Test
	@DisplayName("로그인 성공")
	void  login() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("login")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("login"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
		;

		LoginRequestDto loginRequestDto = new LoginRequestDto("login", "password");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();
		String token = mvcResult.getResponse().getHeader("Authorization");

		boolean validateToken = jwtUtil.validateToken(token);
		Assertions.assertTrue(validateToken);
	}

	@Test
	@DisplayName("잘못된 비밀번호 로그인 실패")
	void  invalidPwLoginFail() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("loginFail")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("loginFail"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
		;

		LoginRequestDto wrongPasswordRequest = new LoginRequestDto("loginFail", "wrongpassword");
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongPasswordRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
				.andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
	}

	@Test
	@DisplayName("잘못된 아이디 로그인 실패")
	void  invalidIdLoginFail() throws Exception {
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("loginFail")
				.password("password")
				.nickname("test")
				.build();

		// 회원가입 정상 입력
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("loginFail"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
		;

		LoginRequestDto wrongIdRequest = new LoginRequestDto("LoginFail", "password");
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongIdRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
				.andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
	}

	@Test
	@DisplayName("관리자 권한 부여 성공")
	void grantAdminSuccess() throws Exception {
		// 일반 유저 등록
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("normalUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("normalUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"));


		// 관리자 유저 등록
		UserSignupRequestDto reqestAdmin = UserSignupRequestDto.builder()
				.username("adminUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup/admin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqestAdmin)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("adminUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));

		// 관리자 로그인
		LoginRequestDto loginRequestDto = new LoginRequestDto("adminUser", "password");
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();
		// 관리자 JWT토큰 값

		String authorization = mvcResult.getResponse().getHeader("Authorization");

		// 관리자가 normalUser role관리자로 변경 성공
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/normalUser/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", authorization))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("normalUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));
	}


	@Test
	@DisplayName("관리자 권한 부여 실패")
	void  grantAdminFail() throws Exception {
		// 일반 유저 등록
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("normalUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect((ResultMatcher) status().isOk())
				.andExpect(jsonPath("$.username").value("normalUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"));
		// 일반유저 로그인
		LoginRequestDto loginRequest = new LoginRequestDto("normalUser", "password");
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();
		// 일반유저 JWT토큰 값
		String authorization = mvcResult.getResponse().getHeader("Authorization");
		// 일반유저가 role관리자로 변경 (접근 제한)
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/normalUser/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", authorization))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
				.andExpect(jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
	}

	@Test
	@DisplayName("존재하지 않는 유저 관리자 권한 부여 실패")
	void  grantAdminForNotExistUserFail() throws Exception {
		// 관리자 유저 등록
		UserSignupRequestDto reqestAdmin = UserSignupRequestDto.builder()
				.username("adminUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup/admin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqestAdmin)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("adminUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));

		// 관리자 로그인
		LoginRequestDto loginRequestDto = new LoginRequestDto("adminUser", "password");
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();
		// 관리자 JWT토큰 값
		String authorization = mvcResult.getResponse().getHeader("Authorization");

		// 관리자가 normalUser role관리자로 변경 성공
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/notuser/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", authorization))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.error.message").value("해당 유저가 없습니다."));
	}

	@Test
	@DisplayName("토큰없이 접근")
	void requestWithoutToken() throws Exception {
		// 일반 유저 등록
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("normalUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("normalUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"));


		// 관리자 유저 등록
		UserSignupRequestDto reqestAdmin = UserSignupRequestDto.builder()
				.username("adminUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup/admin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqestAdmin)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("adminUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));

		// 관리자 로그인
		LoginRequestDto loginRequestDto = new LoginRequestDto("adminUser", "password");
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();
		// 관리자 JWT토큰 값
		String authorization = mvcResult.getResponse().getHeader("Authorization");

		// 토큰없이 접근
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/normalUser/roles")
						.contentType(MediaType.APPLICATION_JSON)
						)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
				.andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."
				));
	}

	@Test
	@DisplayName("만료된 토큰 접근")
	void requestWithExpiredToken() throws Exception {
		// 일반 유저 등록
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("normalUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("normalUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"));


		// 관리자 유저 등록
		UserSignupRequestDto reqestAdmin = UserSignupRequestDto.builder()
				.username("adminUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup/admin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqestAdmin)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("adminUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));


		String authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTQ0OTU3OSwiZXhwIjoxNzQ5NDQ5NTgwfQ.NNXwzhI49_H3LfbKYaapPedNAgVrLg8SQxNyraQrDYM";

		// 만료된 토큰 접근
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/normalUser/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", authorization)
				)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
				.andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."
				));
	}


	@Test
	@DisplayName("잘못된 형식의 토큰 접근")
	void requestWithWrongToken() throws Exception {
		// 일반 유저 등록
		UserSignupRequestDto reqest = UserSignupRequestDto.builder()
				.username("normalUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("normalUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("USER"));


		// 관리자 유저 등록
		UserSignupRequestDto reqestAdmin = UserSignupRequestDto.builder()
				.username("adminUser")
				.password("password")
				.nickname("test")
				.build();
		mockMvc.perform(MockMvcRequestBuilders.post("/signup/admin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reqestAdmin)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("adminUser"))
				.andExpect(jsonPath("$.nickname").value("test"))
				.andExpect(jsonPath("$.roles").isArray())
				.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));


		// Bearer 없는 잘못된 형식의 토큰
		String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTQ0OTU3OSwiZXhwIjoxNzQ5NDQ5NTgwfQ.NNXwzhI49_H3LfbKYaapPedNAgVrLg8SQxNyraQrDYM";

		// 만료된 토큰 접근
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/normalUser/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", authorization)
				)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").exists())
				.andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
				.andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."
				));
	}


	static class LoginRequestDto {
		private String username;
		private String password;

		LoginRequestDto(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return username;
		}
		public String getPassword() {
			return password;
		}
	}


}