package com.sparta.spartascheduling.exception.customException;

import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CounselException extends RuntimeException {
	private final ExceptionCode exceptionCode;
	private final String message;

	public CounselException(ExceptionCode exceptionCode, Object... args) {
		this.exceptionCode = exceptionCode;
		this.message = formatMessage(exceptionCode.getMessage(), args);
	}

	private String formatMessage(String template, Object... args) {
		if (args == null || args.length == 0) {
			return template;
		}
		for (Object arg : args) {
			template = template.replaceFirst("\\{.*?}", arg.toString());
		}
		return template;
	}
}
