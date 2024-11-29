package com.sparta.spartascheduling.common.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sparta.spartascheduling.common.annotation.Auth;
import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.exception.customException.CustomAuthException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
		boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

		// @Auth 어노테이션과 AuthUser 타입이 함께 사용되지 않은 경우 예외 발생
		if (hasAuthAnnotation != isAuthUserType) {
			throw new CustomAuthException(ExceptionCode.ANNOTATION_NOT_FOUND);
		}

		return hasAuthAnnotation;
	}

	@Override
	public Object resolveArgument(
		@Nullable MethodParameter parameter,
		@Nullable ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		@Nullable WebDataBinderFactory binderFactory
	) {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

		// JwtFilter에서 set 한 userId, email, username, userType 값을 가져옴
		Long userId = (Long)request.getAttribute("userId");
		String email = (String)request.getAttribute("email");
		String username = (String)request.getAttribute("username");
		String userType = (String)request.getAttribute("userType");

		return new AuthUser(userId, email, username, userType);
	}
}
