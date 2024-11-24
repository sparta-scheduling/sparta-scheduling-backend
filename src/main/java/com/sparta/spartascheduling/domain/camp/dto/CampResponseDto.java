package com.sparta.spartascheduling.domain.camp.dto;

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

	// 정적 팩토리 메서드 추가
	public static CampResponseDto from(Camp camp, int remainCount) {
		return new CampResponseDto(
			camp.getId(),
			camp.getName(),
			camp.getContents(),
			camp.getStatus(),
			camp.getOpenDate(),
			camp.getCloseDate(),
			remainCount,
			camp.getMaxCount(),
			camp.getCreatedAt(),
			camp.getModifiedAt()
		);
	}

	// 캠프 생성 시 remainCount가 필요 없을 경우를 위한 메서드 오버로딩
	public static CampResponseDto from(Camp camp) {
		return new CampResponseDto(
			camp.getId(),
			camp.getName(),
			camp.getContents(),
			camp.getStatus(),
			camp.getOpenDate(),
			camp.getCloseDate(),
			camp.getMaxCount(), // remainCount를 maxCount로 설정
			camp.getMaxCount(),
			camp.getCreatedAt(),
			camp.getModifiedAt()
		);
	}
}
