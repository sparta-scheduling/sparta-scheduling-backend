package com.sparta.spartascheduling.domain.counsel.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record CounselRequest(
	Long tutorId,
	String content,
	LocalDateTime dateTime
) {
}
