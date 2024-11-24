package com.sparta.spartascheduling.domain.camp.entity;

import com.sparta.spartascheduling.common.entity.Timestamped;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.manager.entity.Manager;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "camps", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"name", "open_date"})
})
public class Camp extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String contents;

	@Column(name = "open_date", nullable = false)
	private LocalDate openDate;

	@Column(name = "close_date", nullable = false)
	private LocalDate closeDate;

	@Enumerated(EnumType.STRING)
	private CampStatus status;

	@Column(name = "max_count", nullable = false)
	private int maxCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	// 정적 팩토리 메서드로 캠프 생성 및 유효성 검사
	public static Camp createCamp(CampRequestDto requestDto, Manager manager) {
		validateCampDates(requestDto.getOpenDate(), requestDto.getCloseDate());
		validateMaxCount(requestDto.getMaxCount());

		Camp camp = Camp.builder()
			.name(requestDto.getName())
			.contents(requestDto.getContents())
			.openDate(requestDto.getOpenDate())
			.closeDate(requestDto.getCloseDate())
			.maxCount(requestDto.getMaxCount())
			.manager(manager)
			.status(CampStatus.CREATED)
			.build();

		camp.updateStatus(); // 생성 시 상태 업데이트

		return camp;
	}

	// 캠프 날짜 유효성 검사 메서드
	private static void validateCampDates(LocalDate openDate, LocalDate closeDate) {
		if (openDate.isAfter(closeDate)) {
			throw new IllegalArgumentException("시작일은 종료일보다 빠르거나 같아야 합니다.");
		}
		if (openDate.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("시작일은 오늘 이후여야 합니다.");
		}
	}

	// 최대 인원 유효성 검사 메서드
	private static void validateMaxCount(int maxCount) {
		if (maxCount <= 0) {
			throw new IllegalArgumentException("최대 인원은 1명 이상이어야 합니다.");
		}
	}

	// 상태 업데이트 메서드
	public void updateStatus() {
		LocalDate today = LocalDate.now();

		if (today.isBefore(this.openDate.minusDays(7))) {
			this.status = CampStatus.CREATED; // 모집 전
		} else if (today.isBefore(this.openDate)) {
			this.status = CampStatus.RECRUITING; // 모집 중
		} else if (today.isBefore(this.closeDate) || today.isEqual(this.closeDate)) {
			this.status = CampStatus.IN_PROGRESS; // 진행 중
		} else {
			this.status = CampStatus.CLOSED; // 종료
		}
	}

	// 남은 인원수 계산 메서드
	public int calculateRemainCount(int participantCount) {
		return this.maxCount - participantCount;
	}

	public void setId(long l) { // 테스트용
	}
}
