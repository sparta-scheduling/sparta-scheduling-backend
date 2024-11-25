// package com.sparta.spartascheduling.domain.camp.service;
//
// import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
// import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
// import com.sparta.spartascheduling.domain.camp.entity.Camp;
// import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
// import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
// import com.sparta.spartascheduling.domain.manager.entity.Manager;
// import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
//
// import java.time.LocalDate;
// import java.util.Optional;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// class CampServiceTest {
//
// 	@Mock
// 	private CampRepository campRepository;
//
// 	@Mock
// 	private ManagerRepository managerRepository;
//
// 	@InjectMocks
// 	private CampService campService;
//
// 	@BeforeEach
// 	void setUp() {
// 		MockitoAnnotations.openMocks(this);
// 	}
//
// 	@Test
// 	void createCamp_Success() {
// 		// Given
// 		CampRequestDto requestDto = new CampRequestDto(
// 			"Test Camp",
// 			"Test Content",
// 			LocalDate.of(2024, 1, 1),
// 			LocalDate.of(2024, 1, 10),
// 			20
// 		);
//
// 		Manager manager = Manager.createManager("manager@test.com", "password", "Test Manager");
//
// 		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
// 		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);
//
// 		ArgumentCaptor<Camp> captor = ArgumentCaptor.forClass(Camp.class);
// 		when(campRepository.save(any(Camp.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
// 		// When
// 		CampResponseDto responseDto = campService.createCamp(requestDto, 1L);
//
// 		// Then
// 		assertNotNull(responseDto);
// 		assertEquals("Test Camp", responseDto.getName());
// 		assertEquals(CampStatus.CREATED, responseDto.getStatus());
//
// 		verify(campRepository, times(1)).save(captor.capture());
// 		Camp capturedCamp = captor.getValue();
// 		assertEquals("Test Camp", capturedCamp.getName());
// 		assertEquals(LocalDate.of(2024, 1, 1), capturedCamp.getOpenDate());
// 	}
//
// 	@Test
// 	void createCamp_ManagerNotFound() {
// 		// Given
// 		CampRequestDto requestDto = new CampRequestDto(
// 			"Test Camp",
// 			"Test Content",
// 			LocalDate.of(2024, 1, 1),
// 			LocalDate.of(2024, 1, 10),
// 			20
// 		);
//
// 		when(managerRepository.findById(1L)).thenReturn(Optional.empty());
//
// 		// When & Then
// 		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
// 			campService.createCamp(requestDto, 1L);
// 		});
//
// 		assertEquals("매니저를 찾을 수 없습니다.", exception.getMessage());
// 		verify(campRepository, never()).save(any(Camp.class));
// 	}
//
// 	@Test
// 	void createCamp_DuplicateCamp() {
// 		// Given
// 		CampRequestDto requestDto = new CampRequestDto(
// 			"Duplicate Camp",
// 			"Test Content",
// 			LocalDate.of(2024, 1, 1),
// 			LocalDate.of(2024, 1, 10),
// 			20
// 		);
//
// 		Manager manager = Manager.createManager("manager@test.com", "password", "Test Manager");
//
// 		when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
// 		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(true);
//
// 		// When & Then
// 		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
// 			campService.createCamp(requestDto, 1L);
// 		});
//
// 		assertEquals("같은 이름과 시작일의 캠프가 이미 존재합니다.", exception.getMessage());
// 		verify(campRepository, never()).save(any(Camp.class));
// 	}
// }
