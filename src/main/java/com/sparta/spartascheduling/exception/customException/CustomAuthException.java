package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CustomAuthException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public CustomAuthException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
