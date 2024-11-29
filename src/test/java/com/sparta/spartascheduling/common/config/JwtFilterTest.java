package com.sparta.spartascheduling.common.config;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JwtFilterTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private JwtFilter jwtFilter;

	// 테스트용 JWT 토큰
	private final String validAdminJwt = "valid.admin.jwt";
	private final String validTutorJwt = "valid.tutor.jwt";
	private final String validUserJwt = "valid.user.jwt";
	private final String invalidJwt = "invalid.jwt.token";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// 유효한 Admin 토큰
		Claims adminClaims = Jwts.claims().setSubject("1");
		adminClaims.put("email", "admin@example.com");
		adminClaims.put("username", "adminUser");
		adminClaims.put("userType", "ADMIN");
		when(jwtUtil.validateToken(validAdminJwt)).thenReturn(true);
		when(jwtUtil.extractClaims(validAdminJwt)).thenReturn(adminClaims);

		// 유효한 Tutor 토큰
		Claims tutorClaims = Jwts.claims().setSubject("2");
		tutorClaims.put("email", "tutor@example.com");
		tutorClaims.put("username", "tutorUser");
		tutorClaims.put("userType", "TUTOR");
		when(jwtUtil.validateToken(validTutorJwt)).thenReturn(true);
		when(jwtUtil.extractClaims(validTutorJwt)).thenReturn(tutorClaims);

		// 유효한 User 토큰
		Claims userClaims = Jwts.claims().setSubject("3");
		userClaims.put("email", "user@example.com");
		userClaims.put("username", "normalUser");
		userClaims.put("userType", "USER");
		when(jwtUtil.validateToken(validUserJwt)).thenReturn(true);
		when(jwtUtil.extractClaims(validUserJwt)).thenReturn(userClaims);

		// 유효하지 않은 토큰
		when(jwtUtil.validateToken(invalidJwt)).thenReturn(false);
	}

	@Test
	void testAdminAccessAllowed() throws Exception {
		mockMvc.perform(get("/admin/resource")
				.header("Authorization", "Bearer " + validAdminJwt))
			.andExpect(status().isOk()); // Admin 권한으로 요청 성공
	}

	@Test
	void testAdminAccessDeniedForUser() throws Exception {
		mockMvc.perform(get("/admin/resource")
				.header("Authorization", "Bearer " + validUserJwt))
			.andExpect(status().isForbidden()) // User 권한으로 Admin 요청 실패
			.andExpect(content().string(containsString("관리자 권한이 없습니다.")));
	}

	@Test
	void testTutorAccessAllowed() throws Exception {
		mockMvc.perform(get("/tutor/resource")
				.header("Authorization", "Bearer " + validTutorJwt))
			.andExpect(status().isOk()); // Tutor 권한으로 요청 성공
	}

	@Test
	void testTutorAccessDeniedForUser() throws Exception {
		mockMvc.perform(get("/tutor/resource")
				.header("Authorization", "Bearer " + validUserJwt))
			.andExpect(status().isForbidden()) // User 권한으로 Tutor 요청 실패
			.andExpect(content().string(containsString("튜터 권한이 없습니다.")));
	}

	@Test
	void testUserAccessAllowed() throws Exception {
		mockMvc.perform(get("/random/resource")
				.header("Authorization", "Bearer " + validUserJwt))
			.andExpect(status().isOk()); // User 권한으로 일반 요청 성공
	}

	@Test
	void testInvalidJwtBlocksRequest() throws Exception {
		mockMvc.perform(get("/random/resource")
				.header("Authorization", "Bearer " + invalidJwt))
			.andExpect(status().isBadRequest()) // 유효하지 않은 토큰 요청 실패
			.andExpect(content().string(containsString("토큰 에러")));
	}
}