package com.sparta.spartascheduling.exception;

import static com.sparta.spartascheduling.exception.enums.ExceptionCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.customException.CounselException;
import com.sparta.spartascheduling.exception.customException.CustomAuthException;
import com.sparta.spartascheduling.exception.customException.ManagerException;
import com.sparta.spartascheduling.exception.customException.TutorException;
import com.sparta.spartascheduling.exception.customException.UserCampException;
import com.sparta.spartascheduling.exception.customException.UserException;
import com.sparta.spartascheduling.exception.dto.NotValidRequestParameterDto;
import com.sparta.spartascheduling.exception.dto.NotValidRequestParameterDto.NotValidParameter;
import com.sparta.spartascheduling.exception.dto.ResponseExceptionDto;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ControllerException")
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomAuthException.class)
	public ResponseEntity<Object> handleAuthException(final CustomAuthException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode.getHttpStatus(), e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	@ExceptionHandler(CampException.class)
	public ResponseEntity<Object> handleCampException(final CampException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode.getHttpStatus(), e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	@ExceptionHandler(CounselException.class)
	public ResponseEntity<Object> handleCounselException(final CounselException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode.getHttpStatus(), e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	@ExceptionHandler(ManagerException.class)
	public ResponseEntity<Object> handleManagerException(final ManagerException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode, e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	@ExceptionHandler(TutorException.class)
	public ResponseEntity<Object> handleTutorException(final TutorException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode.getHttpStatus(), e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	@ExceptionHandler(UserCampException.class)
	public ResponseEntity<Object> handleUserCampException(final UserCampException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode.getHttpStatus(), e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<Object> handleUserException(final UserException e) {
		ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{}, {}", exceptionCode.getHttpStatus(), e.getExceptionCode());
		return ResponseEntity.status(exceptionCode.getHttpStatus())
			.body(makeResponseExceptionDto(exceptionCode));
	}

	private ResponseExceptionDto makeResponseExceptionDto(ExceptionCode exceptionCode) {
		return ResponseExceptionDto.builder()
			.code(exceptionCode.getHttpStatus().toString())
			.message(exceptionCode.getMessage())
			.build();
	}

	// dto 입력 예외처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ExceptionCode exceptionCode = INVALID_REQUEST_PARAMETER;
		log.error("{} : {}", exceptionCode.getHttpStatus(), exceptionCode.getMessage());
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
