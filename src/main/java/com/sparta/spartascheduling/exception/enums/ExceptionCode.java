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

	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

	// 회원-학생 관련 예외 (user)
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 학생입니다."),

	USER_WITHDRAWN(HttpStatus.BAD_REQUEST, "탈퇴한 유저입니다."),

	NO_AUTHORIZATION_USER(HttpStatus.FORBIDDEN, "학생 권한이 없습니다."),

	// 캠프 관련 예외 (camp)
	EXCEEDED_CAMP_CAPACITY(HttpStatus.BAD_REQUEST, "정원이 초과되어서 캠프를 신청할 수 없습니다."),

	NOT_FOUND_CAMP(HttpStatus.NOT_FOUND, "캠프가 존재하지 않습니다."),

	ALREADY_JOIN_CAMP(HttpStatus.BAD_REQUEST, "현재 참여중인 캠프가 있어서 신청할 수 없습니다"),

	ALREADY_EXIST_CAMP(HttpStatus.BAD_REQUEST, "같은 이름과 시작일의 캠프가 이미 존재합니다."),

	ALREADY_APPLY_CAMP(HttpStatus.BAD_REQUEST, "중복된 캠프입니다.(신청한 이력이 존재하는 캠프입니다.)"),

	START_DATE_BEFORE_TODAY(HttpStatus.BAD_REQUEST, "시작일은 오늘 이후여야 합니다."),

	START_DATE_AFTER_END_DATE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 빠르거나 같아야 합니다."),

	INVALID_MAX_COUNT(HttpStatus.BAD_REQUEST, "최대 인원은 1명 이상이어야 합니다."),
	// 회원과 캠프 관계 관련 예외 (userCamp)

	NOT_FOUND_USER_CAMP(HttpStatus.NOT_FOUND, "유저에 맵핑된 캠프의 정보를 찾을수 없습니다."),

	// 상담 관련 예외 (counsel)
	NOT_FOUND_COUNSEL(HttpStatus.NOT_FOUND, "상담이 없습니다."),

	TUTOR_NOT_IN_CAMP(HttpStatus.BAD_REQUEST, "캠프에 있는 튜터에게만 신청이 가능합니다."),

	CONSULTATION_IN_PROGRESS(HttpStatus.BAD_REQUEST, "이미 진행 중인 상담이 있습니다. 새로운 상담을 신청할 수 없습니다."),

	INVALID_CONSULT_DATE(HttpStatus.BAD_REQUEST, "상담을 신청할 수 없는 날짜입니다."),

	INVALID_CONSULT_TIME(HttpStatus.BAD_REQUEST, "상담 시간이 아닙니다. 상담 가능 시간: {start} ~ {end}"),

	// 매니저 (ADMIN) 관련 예외 (manager)
	NO_AUTHORIZATION_ADMIN(HttpStatus.FORBIDDEN, "ADMIN 권한이 필요합니다."),

	NOT_FOUND_MANAGER(HttpStatus.NOT_FOUND, "매니저를 찾을 수 없습니다."),

	// 튜터 관련 예외 (tutor)
	NOT_FOUND_TUTOR(HttpStatus.NOT_FOUND, "존재하지 않는 튜터입니다."),

	NO_AUTHORIZATION_TUTOR(HttpStatus.FORBIDDEN, "TUTOR 권한이 필요합니다."),

	ALREADY_EXIST_COUNSEL(HttpStatus.BAD_REQUEST, "이미 상담이 존재하여 변경이 어렵습니다."),

	// 복수 권한 관련 예외
	NO_HAVE_AUTH_DELETE(HttpStatus.FORBIDDEN, "매니저가 아니거나 자신이 아닌 사람을 삭제 할 수 없습니다."),

	// 락 타임아웃 예외 처리
	CAMP_LOCK_TIMEOUT(HttpStatus.CONFLICT, "캠프 신청 중 타임아웃이 발생했습니다");

	private final HttpStatus httpStatus;
	private final String message;

	ExceptionCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

}
