package com.sparta.spartascheduling.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	// requestDto 입력 예외
	INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "올바르지 않은 입력 값이 포함되어 있습니다."),

	//JwtUtil 예외처리
	SECRET_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT secret key is mandatory"),

	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found Token"),

	// AuthUserArgumentResolver 예외 처리
	ANNOTATION_NOT_FOUND(HttpStatus.NOT_FOUND, "@Auth와 AuthUser 타입은 함께 사용되어야 합니다."),

	// 회원 가입 및 인증 과정 예외 (auth)
	DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

	NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 동일하게 입력해 주십시오."),

	// 회원-학생 관련 예외 (user)
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 학생입니다."),

	// 캠프 관련 예외 (camp)
	EXCEEDED_CAMP_CAPACITY(HttpStatus.BAD_REQUEST, "이미 남은인원이 0입니다."),

	NOT_FOUND_CAMP(HttpStatus.NOT_FOUND, "캠프가 존재하지 않습니다."),

	ALREADY_JOIN_CAMP(HttpStatus.BAD_REQUEST, "정원이 초과되어서 캠프를 신청할 수 없습니다."),

	// 회원과 캠프 관계 관련 예외 (userCamp)

	// 상담 관련 예외 (counsel)

	// 매니저 관련 예외 (manager)

	// 튜터 관련 예외 (tutor)
	TUTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 튜터입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

}
