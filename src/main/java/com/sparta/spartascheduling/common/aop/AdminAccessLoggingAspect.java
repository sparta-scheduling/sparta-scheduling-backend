package com.sparta.spartascheduling.common.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.sparta.spartascheduling.common.config.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "aop Logging")
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessLoggingAspect {

	private final HttpServletRequest request;
	private final JwtUtil jwtUtil;

	@Before("execution(* com.sparta.spartascheduling.domain..controller..*(..)) &&" +
		"!execution(* com.sparta.spartascheduling.domain.auth.controller..*(..))")
	public void logging(JoinPoint joinPoint) {

		String authorization = request.getHeader("Authorization");
		String token = jwtUtil.substringToken(authorization);

		Claims claims = jwtUtil.extractClaims(token);

		String userEmail = claims.get("email", String.class);
		String username = claims.get("username", String.class);
		String userType = claims.get("userType", String.class);
		LocalDateTime requestTime = LocalDateTime.now();
		String requestUrl = request.getRequestURI();

		log.info(
			"User Login log = User Email :{}, Username: {}, UserType: {}, Request Time :{}, Request URL: {}, HTTPMethod: {}, Method: {}",
			userEmail, username, userType, requestTime, requestUrl, request.getMethod(),
			joinPoint.getSignature().getName());
	}
}
