package com.sparta.spartascheduling.domain.camp.entity;

import java.time.LocalDate;

import com.sparta.spartascheduling.common.entity.Timestamped;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import jakarta.persistence.*;
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

	@Column(name = "remain_count", nullable = false)
	private int remainCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	// 캠프 생성 로직
	public static Camp createCamp(String name, String contents, LocalDate openDate, LocalDate closeDate, int maxCount,
		Manager manager) {
		validateCampDates(openDate, closeDate);
		validateMaxCount(maxCount);

		return Camp.builder() // 초기상태 CREATED 고정이기에 updateStatus 호출 할 필요없어서 수정
			.name(name)
			.contents(contents)
			.openDate(openDate)
			.closeDate(closeDate)
			.maxCount(maxCount)
			.remainCount(maxCount) // 초기 남은 인원은 최대 인원으로 설정
			.manager(manager)
			.status(CampStatus.CREATED)
			.build();
	}

	// 날짜 유효성 검사
	private static void validateCampDates(LocalDate openDate, LocalDate closeDate) {
		if (openDate.isAfter(closeDate)) {
			throw new CampException(ExceptionCode.START_DATE_AFTER_END_DATE);
		}
		if (openDate.isBefore(LocalDate.now())) {
			throw new CampException(ExceptionCode.START_DATE_BEFORE_TODAY);
		}
	}

	// 최대 인원 유효성 검사
	private static void validateMaxCount(int maxCount) {
		if (maxCount <= 0) {
			throw new CampException(ExceptionCode.INVALID_MAX_COUNT);
		}
	}

	// 상태 업데이트
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

	// 남은 인원 감소
	public void decreaseRemainCount() {
		if (remainCount <= 0) {
			throw new CampException(ExceptionCode.EXCEEDED_CAMP_CAPACITY);
		}
		this.remainCount--;
	}
}
