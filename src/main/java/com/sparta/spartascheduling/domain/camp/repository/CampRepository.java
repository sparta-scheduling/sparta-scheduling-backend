package com.sparta.spartascheduling.domain.camp.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.camp.entity.Camp;

public interface CampRepository extends JpaRepository<Camp, Long> {
	// 중복 여부 확인 메서드
	boolean existsByNameAndOpenDate(String name, LocalDate openDate);
}
