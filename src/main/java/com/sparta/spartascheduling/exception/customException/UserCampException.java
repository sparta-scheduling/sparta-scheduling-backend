package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class UserCampException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public UserCampException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
