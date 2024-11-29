package com.sparta.spartascheduling.domain.camp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.exception.customException.ManagerException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

@ExtendWith(MockitoExtension.class)
class CampServiceTest {

	@Mock
	private CampRepository campRepository;

	@Mock
	private ManagerRepository managerRepository;

	@InjectMocks
	private CampService campService;

	private AuthUser adminUser;
	private AuthUser regularUser;

	@BeforeEach
	void setup() {
		adminUser = new AuthUser(1L, "admin@test.com", "Admin", "ADMIN");
		regularUser = new AuthUser(2L, "user@test.com", "User", "USER");
	}

	@Test
	void testCreateCamp_Success() {
		// Given
		Manager mockManager = new Manager(1L, "admin@test.com", "encodedPassword", "Admin");
		CampRequestDto requestDto = new CampRequestDto(
			"Spring Camp",
			"Description",
			LocalDate.of(2024,12,5),
			LocalDate.of(2024,12,15),
			50
		);
		Camp camp = new Camp(
			null,
			"Spring Camp",
			"Description",
			LocalDate.of(2024,12,5),
			LocalDate.of(2024,12,15),
			CampStatus.CREATED,
			50,
			mockManager,
			1
		);
		when(managerRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(mockManager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);
		when(campRepository.save(any())).thenReturn(camp);


		// When
		CampResponseDto responseDto = campService.createCamp(requestDto, adminUser);

		// Then
		assertNotNull(responseDto);
		assertEquals("Spring Camp", responseDto.getName());
		assertEquals(50, responseDto.getMaxCount());
		assertEquals(CampStatus.CREATED, responseDto.getStatus());
		verify(campRepository, times(1)).save(any(Camp.class));
	}

	@Test
	void testCreateCamp_ManagerNotFound() {
		// Given
		CampRequestDto requestDto = new CampRequestDto(
			"Spring Camp",
			"Description",
			LocalDate.now().plusDays(1),
			LocalDate.now().plusDays(10),
			50
		);

		when(managerRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.empty());

		// When & Then
		ManagerException exception = assertThrows(
			ManagerException.class,
			() -> campService.createCamp(requestDto, adminUser)
		);
		assertEquals(ExceptionCode.NOT_FOUND_MANAGER, exception.getExceptionCode());
	}

	@Test
	void testGetCampById_Success() {
		// Given
		Manager manager = new Manager(1L, "admin@test.com", "encodedPassword", "Admin");
		Camp mockCamp = new Camp(
			null, // id를 초기화하지 않음
			"Spring Camp",
			"Description",
			LocalDate.of(2024,12,5),
			LocalDate.of(2024,12,15),
			CampStatus.CREATED,
			50,
			manager,
			1
		);

		// Reflection을 통해 id 값을 강제로 설정
		ReflectionTestUtils.setField(mockCamp, "id", 1L);

		when(campRepository.findById(1L)).thenReturn(Optional.of(mockCamp));

		// When
		CampResponseDto responseDto = campService.getCampById(1L);

		// Then
		assertNotNull(responseDto);
		assertEquals(1L, responseDto.getId());
		assertEquals("Spring Camp", responseDto.getName());
		verify(campRepository, times(1)).findById(1L);
	}

	// @Test
	// void testGetCampById_NotFound() {
	// 	// Given
	// 	when(campRepository.findById(1L)).thenReturn(Optional.empty());
	//
	// 	// When & Then
	// 	CampException exception = assertThrows(
	// 		CampException.class,
	// 		() -> campService.getCampById(1L)
	// 	);
	// 	assertEquals(ExceptionCode.NOT_FOUND_CAMP, exception.getExceptionCode());
	// }

}
