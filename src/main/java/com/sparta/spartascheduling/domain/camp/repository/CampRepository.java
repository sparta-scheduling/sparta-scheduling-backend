package com.sparta.spartascheduling.domain.camp.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.camp.entity.Camp;

public interface CampRepository extends JpaRepository<Camp, Long> {
	boolean existsByNameAndOpenDate(String name, LocalDate openDate);
}
