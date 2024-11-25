package com.sparta.spartascheduling.exception;

import static com.sparta.spartascheduling.exception.enums.ExceptionCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.spartascheduling.exception.dto.NotValidRequestParameterDto;
import com.sparta.spartascheduling.exception.dto.NotValidRequestParameterDto.NotValidParameter;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ControllerException")
@RestControllerAdvice
public class GlobalExceptionHandler {

	// dto 입력 예외처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ExceptionCode exceptionCode = INVALID_REQUEST_PARAMETER;
		log.error("{} : {}", exceptionCode, exceptionCode.getMessage());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(notValidRequestParameterDto(e, exceptionCode));

	}

	private NotValidRequestParameterDto notValidRequestParameterDto(BindException e,
		ExceptionCode exceptionCode) {

		List<NotValidRequestParameterDto.NotValidParameter> notValidParameters = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(NotValidParameter::of)
			.toList();

		return NotValidRequestParameterDto.builder()
			.code(exceptionCode.name())
			.message(exceptionCode.getMessage())
			.notValidParameters(notValidParameters)
			.build();

	}

}
