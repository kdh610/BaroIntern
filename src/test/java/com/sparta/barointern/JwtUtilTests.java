package com.sparta.barointern;

import com.sparta.barointern.application.UserService;
import com.sparta.barointern.domain.entity.Role;
import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.domain.repository.UserRepository;
import com.sparta.barointern.infrastructure.exception.BaseException;
import com.sparta.barointern.infrastructure.exception.Code;
import com.sparta.barointern.infrastructure.jwt.JwtAuthorizationFilter;
import com.sparta.barointern.infrastructure.jwt.JwtUtil;
import com.sparta.barointern.presentation.dto.request.UserSignupRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


@SpringBootTest
@AutoConfigureMockMvc
class JwtUtilTests {

	@Autowired
	private JwtUtil jwtUtil;


	private final String WRONG_SECRET_KEY = "this-is-a-completely-different-fake-secret-key-67890";

	@Test
	@DisplayName("서명 검증 (서버의 비밀 키로 서명 확인)")
	void validateToken_withInvalidSignature_shouldThrowException() {
		JwtUtil attackerJwtUtil = new JwtUtil(WRONG_SECRET_KEY);
		String invalidSignatureToken = attackerJwtUtil.createJwtToken("testUser", Role.create(UserRole.USER), "access");

		BaseException exception = assertThrows(BaseException.class, () -> {
			jwtUtil.validateToken(invalidSignatureToken);
		});

		assertThat(exception.getCode()).isEqualTo(Code.INVALID_TOKEN);
	}



	@Test
	@DisplayName("Jwt 토큰 생성")
	void createJwtToken(){
		String jwtToken = jwtUtil.createJwtToken("test", Role.create(UserRole.USER), "access");
		System.out.println(jwtToken);
		boolean result = jwtUtil.validateToken(jwtToken);
		Assertions.assertTrue(result);
	}

	@Test
	@DisplayName("Jwt 토큰만료 여부 확인")
	void validateExpiredJwtToken(){
		assertThrows(BaseException.class,
				() -> jwtUtil.validateToken("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTQ0OTU3OSwiZXhwIjoxNzQ5NDQ5NTgwfQ.NNXwzhI49_H3LfbKYaapPedNAgVrLg8SQxNyraQrDYM"));
	}

	@Test
	@DisplayName("Jwt 토큰 존재 여부 확인")
	void validateNoJwtToken(){
		assertThrows(BaseException.class,
				() -> jwtUtil.validateToken(null));
	}

	@Test
	@DisplayName("Jwt 잘못된 형식의 토큰 검증")
	void validateWrongJwtToken(){
		assertThrows(BaseException.class,
				() -> jwtUtil.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTQ0OTU3OSwiZXhwIjoxNzQ5NDQ5NTgwfQ.NNXwzhI49_H3LfbKYaapPedNAgVrLg8SQxNyraQrDYM"));
	}

	@Test
	@DisplayName("Jwt 토큰에 포함된 권한 정보 추출")
	void extractAuthorizationJwtToken(){
		String role = jwtUtil.getRole("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0IiwiY2F0ZWdvcnkiOiJhY2Nlc3MiLCJyb2xlIjp7InJvbGUiOiJVU0VSIn0sImlhdCI6MTc0OTUzMjIwMiwiZXhwIjoxNzUwMTM3MDAyfQ.staiAHqU0jMTfUzAOP0KjF0JHhbL81-NgM49RBgp-Sk"
	);
		System.out.println(role);
		assertEquals(role,"USER");

	}

	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	@Autowired
	private UserService userService;

	// Mock 객체들
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private FilterChain filterChain;
	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		filterChain = mock(FilterChain.class); // Mockito로 가짜 FilterChain 생성
	}

	@AfterEach
	void tearDown() {
		userService.deleteAll();
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("SecurityContext에 인증 정보 설정")
	void givenValidJwt_whenFilterRuns_thenSecurityContextIsPopulated() throws ServletException, IOException {
		UserSignupRequestDto user = UserSignupRequestDto.builder()
				.username("testUser")
				.password("password")
				.nickname("test")
				.build();

		userService.signup(user.toAppDto());
		String token = jwtUtil.createJwtToken("testUser", Role.create(UserRole.USER), "access");
		request.addHeader("Authorization", token);


		jwtAuthorizationFilter.doFilter(request, response, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		assertThat(authentication).isNotNull();
		assertThat(authentication.getName()).isEqualTo("testUser");
		assertThat(authentication.getAuthorities()).anyMatch(grantedAuthority ->
				grantedAuthority.getAuthority().equals("ROLE_USER")
		);
	}




}