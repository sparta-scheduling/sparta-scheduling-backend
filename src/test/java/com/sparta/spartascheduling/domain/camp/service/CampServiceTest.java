package com.sparta.spartascheduling.domain.camp.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CampServiceTest {

	@Autowired
	private CampService campService;

	@Autowired
	private CampRepository campRepository;

	@Autowired
	private ManagerRepository managerRepository;

	private AuthUser adminAuthUser;
	private Manager manager;

	@BeforeEach
	void setUp() {
		// 이전 데이터 제거
		managerRepository.deleteAll();
		campRepository.deleteAll();

		// 관리자 생성
		manager = Manager.builder()
			.email("admin@test.com")
			.password("password")
			.username("Admin")
			.build();
		managerRepository.save(manager);

		// AuthUser 설정
		adminAuthUser = new AuthUser(manager.getId(), manager.getEmail(), manager.getUsername(), "ADMIN");
	}

	@Test
	void createCamp_Success() {
		// Given
		CampRequestDto requestDto = new CampRequestDto(
			"Test Camp",
			"Test Content",
			LocalDate.now().plusDays(10),
			LocalDate.now().plusDays(20),
			30
		);

		// When
		CampResponseDto responseDto = campService.createCamp(requestDto, adminAuthUser);

		// Then
		assertThat(responseDto.getName()).isEqualTo("Test Camp");
		assertThat(responseDto.getStatus()).isEqualTo(CampStatus.CREATED);
		assertThat(responseDto.getMaxCount()).isEqualTo(30);
	}

	@Test
	void createCamp_Failure_DuplicateCamp() {
		// Given
		CampRequestDto requestDto = new CampRequestDto(
			"Duplicate Camp",
			"Test Content",
			LocalDate.now().plusDays(10),
			LocalDate.now().plusDays(20),
			30
		);

		campService.createCamp(requestDto, adminAuthUser);

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, adminAuthUser);
		});

		assertThat(exception.getMessage()).isEqualTo("같은 이름과 시작일의 캠프가 이미 존재합니다.");
	}

	@Test
	void createCamp_Failure_NoManager() {
		// Given
		AuthUser invalidAuthUser = new AuthUser(999L, "invalid@test.com", "Invalid", "ADMIN");
		CampRequestDto requestDto = new CampRequestDto(
			"No Manager Camp",
			"Test Content",
			LocalDate.now().plusDays(10),
			LocalDate.now().plusDays(20),
			30
		);

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, invalidAuthUser);
		});

		assertThat(exception.getMessage()).isEqualTo("매니저를 찾을 수 없습니다.");
	}
}
