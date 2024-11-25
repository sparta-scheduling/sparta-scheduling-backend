package com.sparta.spartascheduling.domain.camp.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.camp.entity.Camp;

public interface CampRepository extends JpaRepository<Camp, Long> {
	boolean existsByNameAndOpenDate(String name, LocalDate openDate);

	// 중복 캠프 확인 메서드 추가
	default void checkDuplicateCamp(String name, LocalDate openDate) {
		if (existsByNameAndOpenDate(name, openDate)) {
			throw new IllegalArgumentException("같은 이름과 시작일의 캠프가 이미 존재합니다.");
		}
	}
}
