package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public AuthException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
