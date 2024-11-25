package com.sparta.spartascheduling.domain.tutor.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record TutorRequest (
	@NotNull
	LocalDateTime counselStart,
	@NotNull
	LocalDateTime counselEnd
	) {
}
