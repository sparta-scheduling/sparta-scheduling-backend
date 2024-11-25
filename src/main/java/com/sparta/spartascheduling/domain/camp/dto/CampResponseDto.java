package com.sparta.spartascheduling.domain.camp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;

import lombok.Getter;

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

	// 엔티티를 직접 받아서 매핑
	private CampResponseDto(Camp camp) {
		this.id = camp.getId();
		this.name = camp.getName();
		this.contents = camp.getContents();
		this.status = camp.getStatus();
		this.openDate = camp.getOpenDate();
		this.closeDate = camp.getCloseDate();
		this.remainCount = camp.getRemainCount();
		this.maxCount = camp.getMaxCount();
		this.createdAt = camp.getCreatedAt();
		this.updatedAt = camp.getModifiedAt();
	}

	// 정적 팩토리 메서드 사용. Camp 엔티티를 받아 DTO 생성
	public static CampResponseDto from(Camp camp) {
		return new CampResponseDto(camp);
	}
}
