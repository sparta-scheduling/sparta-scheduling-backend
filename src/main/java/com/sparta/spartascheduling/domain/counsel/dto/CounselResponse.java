package com.sparta.spartascheduling.domain.counsel.dto;

import java.time.LocalDateTime;

import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;

public record CounselResponse(
	Long id,
	Long userId,
	Long tutorId,
	String content,
	LocalDateTime dateTime,
	CounselStatus status
) {
	public static CounselResponse from(Counsel counsel) {
		return new CounselResponse(
			counsel.getId(),
			counsel.getUser().getId(),
			counsel.getTutor().getId(),
			counsel.getContent(),
			counsel.getDatetime(),
			counsel.getStatus()
		);
	}
}
