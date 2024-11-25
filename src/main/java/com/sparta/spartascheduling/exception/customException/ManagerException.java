package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class ManagerException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public ManagerException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
