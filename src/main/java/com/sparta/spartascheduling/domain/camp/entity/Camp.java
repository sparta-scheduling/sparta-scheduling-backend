package com.sparta.spartascheduling.domain.camp.entity;

import java.time.LocalDate;

import com.sparta.spartascheduling.common.entity.Timestamped;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	private int remainCount;

	// 정적 팩토리 메서드로 캠프 생성 및 유효성 검사
	public static Camp createCamp(String name, String contents, LocalDate openDate, LocalDate closeDate, int maxCount,
		Manager manager) {
		validateCampDates(openDate, closeDate);
		validateMaxCount(maxCount);

		Camp camp = Camp.builder()
			.name(name)
			.contents(contents)
			.openDate(openDate)
			.closeDate(closeDate)
			.maxCount(maxCount)
			.manager(manager)
			.remainCount(maxCount) // 남은 자리가 0이 되는 예외 발생을 수정했습니다.
			.status(CampStatus.CREATED)
			.build();

		camp.updateStatus();
		return camp;
	}

	// 캠프 날짜 유효성 검사 메서드
	private static void validateCampDates(LocalDate openDate, LocalDate closeDate) {
		if (openDate.isAfter(closeDate)) {
			throw new CampException(ExceptionCode.START_DATE_AFTER_END_DATE);
		}
		if (openDate.isBefore(LocalDate.now())) {
			throw new CampException(ExceptionCode.START_DATE_BEFORE_TODAY);
		}
	}

	// 최대 인원 유효성 검사 메서드
	private static void validateMaxCount(int maxCount) {
		if (maxCount <= 0) {
			throw new CampException(ExceptionCode.INVALID_MAX_COUNT);
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

	// 캠프신청될때 남은인원 -1
	public void decreaseRemainCount() {
		if (remainCount <= 0) {
			throw new CampException(ExceptionCode.EXCEEDED_CAMP_CAPACITY);
		}
		this.remainCount--;
	}
}
