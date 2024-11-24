package com.sparta.spartascheduling.domain.camp.dto;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CampResponseDto {
	private Long id;
	private String name;
	private String contents;
	private CampStatus status;
	private LocalDate openDate;
	private LocalDate closeDate;
	private int maxCount;
	private LocalDateTime createdAt;

	public CampResponseDto(Camp camp) {
		this.id = camp.getId();
		this.name = camp.getName();
		this.contents = camp.getContents();
		this.status = camp.getStatus();
		this.openDate = camp.getOpenDate();
		this.closeDate = camp.getCloseDate();
		this.maxCount = camp.getMaxCount();
		this.createdAt = camp.getCreatedAt();
	}
}
