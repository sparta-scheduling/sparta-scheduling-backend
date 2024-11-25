package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CounselException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public CounselException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
