package com.sparta.spartascheduling.domain.tutor.dto;

import java.time.LocalTime;

import com.sparta.spartascheduling.domain.tutor.entity.Tutor;

public record TutorResponseDto(
	LocalTime counselStart,
	LocalTime counselEnd
){

	public static TutorResponseDto from(Tutor tutor) {
		return new TutorResponseDto(
			tutor.getCounselStart(),
			tutor.getCounselEnd()
		);
	}
}
