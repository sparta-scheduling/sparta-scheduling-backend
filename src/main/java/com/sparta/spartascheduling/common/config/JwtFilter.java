package com.sparta.spartascheduling.common.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtFilter")
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

	private final JwtUtil jwtUtil;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String url = httpRequest.getRequestURI();

		if (StringUtils.hasText(url) && (url.startsWith("/auth"))) {
			// 회원가입, 로그인 관련 API는 인증 필요없이 요청 진행
			chain.doFilter(request, response);    // 다음 Filter 로 이동
			return;
		}

		String bearerJwt = httpRequest.getHeader("Authorization");

		if (bearerJwt == null) {
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
			return;
		}

		String jwt = jwtUtil.substringToken(bearerJwt);

		if (!jwtUtil.validateToken(jwt)) {
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "토큰 에러");
			return;
		}

		Claims claims = jwtUtil.extractClaims(jwt);
		if (claims == null) {
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
			return;
		}

		String userType = claims.get("userType", String.class);

		httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
		httpRequest.setAttribute("email", claims.get("email"));
		httpRequest.setAttribute("username", claims.get("username"));

		if (url.startsWith("/admin")) {
			// 관리자 권한이 없는 경우 403을 반환합니다.
			if (!userType.equals("ADMIN")) {
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
				return;
			}
			chain.doFilter(request, response);
			return;
		}

		if (url.startsWith("/tutor")) {
			// 튜터 권한이 아닌 경우 403을 반환합니다.
			if (!userType.equals("TUTOR")) {
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "튜터 권한이 없습니다.");
				return;
			}
			chain.doFilter(request, response);
			return;
		}

		chain.doFilter(request, response);
	}
}
