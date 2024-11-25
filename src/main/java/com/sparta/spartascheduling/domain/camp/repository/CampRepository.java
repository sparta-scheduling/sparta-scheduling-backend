package com.sparta.spartascheduling.domain.camp.repository;

import java.time.LocalDate;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import org.springframework.data.jpa.repository.Query;

public interface CampRepository extends JpaRepository<Camp, Long> {
	// 중복 여부 확인 메서드
	boolean existsByNameAndOpenDate(String name, LocalDate openDate);
}
