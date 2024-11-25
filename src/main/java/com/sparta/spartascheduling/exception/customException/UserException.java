package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public UserException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
