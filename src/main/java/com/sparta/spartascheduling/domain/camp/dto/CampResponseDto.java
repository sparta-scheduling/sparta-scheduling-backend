package com.sparta.spartascheduling.domain.camp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter

public class CampResponseDto {
	private Long id;
	private String name;
	private String contents;
	private CampStatus status;
	private LocalDate openDate;
	private LocalDate closeDate;
	private int remainCount;
	private int maxCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private CampResponseDto(Long id, String name, String contents, CampStatus status,
		LocalDate openDate, LocalDate closeDate, int remainCount, int maxCount,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.contents = contents;
		this.status = status;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.remainCount = remainCount;
		this.maxCount = maxCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public CampResponseDto(Long id, String name, String contents, CampStatus status, LocalDate openDate,
		LocalDate closeDate, int maxCount, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.contents = contents;
		this.status = status;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.maxCount = maxCount;
		this.createdAt = createdAt;
	}

	public static CampResponseDto from(Camp camp) {
		return new CampResponseDto(
			camp.getId(),
			camp.getName(),
			camp.getContents(),
			camp.getStatus(),
			camp.getOpenDate(),
			camp.getCloseDate(),
			camp.getMaxCount(),
			camp.getCreatedAt()
		);
	}
}
