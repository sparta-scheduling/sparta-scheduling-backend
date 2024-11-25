package com.sparta.spartascheduling.domain.camp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CampRequestDto {
	private String name;
	private String contents;
	private LocalDate openDate;
	private LocalDate closeDate;
	private int maxCount;
}