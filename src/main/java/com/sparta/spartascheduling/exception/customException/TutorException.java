package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class TutorException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public TutorException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
