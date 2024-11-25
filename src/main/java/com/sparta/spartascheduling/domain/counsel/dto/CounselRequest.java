package com.sparta.spartascheduling.domain.counsel.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record CounselRequest(
	@NotNull
	Long tutorId,
	@NotNull
	String content,
	@NotNull
	LocalDateTime dateTime
) {
}
