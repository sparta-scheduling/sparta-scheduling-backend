package com.sparta.spartascheduling.domain.camp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CampRequestDto {
	private String name;
	private String contents;
	private LocalDate openDate;
	private LocalDate closeDate;
	private int maxCount;
}
