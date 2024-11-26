package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CounselException extends RuntimeException {
	private final ExceptionCode exceptionCode;
	private final String message;
	private final Object[] args;

	public CounselException(ExceptionCode exceptionCode, Object... args) {
		// 예외 메시지를 동적으로 포맷팅하여 설정
		super(formatMessage(exceptionCode.getMessage(), args));
		this.exceptionCode = exceptionCode;
		this.message = formatMessage(exceptionCode.getMessage(), args);  // 동적 메시지
		this.args = args;
	}

	// 메시지를 동적으로 포맷팅하는 헬퍼 메서드
	private static String formatMessage(String message, Object[] args) {
		if (args != null && args.length == 2) {  // 예상되는 인자 갯수 확인
			// {start}와 {end}를 실제 값으로 교체
			return message.replace("{start}", args[0].toString())
				.replace("{end}", args[1].toString());
		}
		return message;  // 기본 메시지 반환
	}
}
