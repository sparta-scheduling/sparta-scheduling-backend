package com.sparta.spartascheduling.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseExceptionDto {
	private String code;
	private String message;
}
