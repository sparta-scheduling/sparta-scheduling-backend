package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CampException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public CampException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
