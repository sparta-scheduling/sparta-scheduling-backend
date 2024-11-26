package com.sparta.spartascheduling.common.aop;

import static org.mockito.Mockito.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sparta.spartascheduling.common.config.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

class AdminAccessLoggingAspectTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private JoinPoint joinPoint;

	@Mock
	private Claims claims;

	@InjectMocks
	private AdminAccessLoggingAspect adminAccessLoggingAspect;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mock 객체 초기화
	}

	@Test
	void AopLoggingTest() {

		when(request.getHeader("Authorization")).thenReturn("Bearer someToken");
		when(request.getRequestURI()).thenReturn("/test/mypage");
		when(request.getMethod()).thenReturn("GET");
		when(jwtUtil.substringToken("Bearer someToken")).thenReturn("someToken");
		when(jwtUtil.extractClaims("someToken")).thenReturn(claims);
		when(claims.get("email", String.class)).thenReturn("testuser@example.com");
		when(claims.get("username", String.class)).thenReturn("testuser");
		when(claims.get("userType", String.class)).thenReturn("ADMIN");

		MethodSignature methodSignature = mock(MethodSignature.class);
		when(joinPoint.getSignature()).thenReturn(methodSignature);
		when(methodSignature.getName()).thenReturn("getDashboard");

		adminAccessLoggingAspect.logging(joinPoint);

		verify(request, times(1)).getRequestURI();
		verify(request, times(1)).getMethod();
		verify(jwtUtil, times(1)).extractClaims("someToken");
		verify(claims, times(1)).get("email", String.class);
		verify(claims, times(1)).get("username", String.class);
		verify(claims, times(1)).get("userType", String.class);
	}

}
