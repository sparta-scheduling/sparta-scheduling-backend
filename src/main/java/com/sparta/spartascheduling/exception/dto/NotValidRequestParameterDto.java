package com.sparta.spartascheduling.exception.dto;

import java.util.List;

import org.springframework.validation.FieldError;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotValidRequestParameterDto {
	private String code;

	private String message;

	private List<NotValidParameter> notValidParameters;

	@Getter
	@Builder
	public static class NotValidParameter {
		private String filed;
		private String message;

		public static NotValidParameter of(FieldError fieldError) {
			return NotValidParameter.builder()
				.filed(fieldError.getField())
				.message(fieldError.getDefaultMessage())
				.build();
		}
	}
}
