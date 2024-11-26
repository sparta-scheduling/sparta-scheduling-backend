package com.sparta.spartascheduling.domain.camp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.customException.ManagerException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

@ExtendWith(MockitoExtension.class)
class CampServiceTest {

	// Mock 객체 선언
	@Mock
	private CampRepository campRepository;

	@Mock
	private ManagerRepository managerRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserCampRepository userCampRepository;

	@Mock
	private EntityManager entityManager; // Mock EntityManager 추가

	@InjectMocks
	private CampService campService;

	// 테스트에 사용할 사용자 인증 정보
	private AuthUser adminUser;
	private AuthUser regularUser;

	// 테스트 시작 전 초기화
	@BeforeEach
	void setup() {
		adminUser = new AuthUser(1L, "admin@test.com", "Admin", "ADMIN");
		regularUser = new AuthUser(2L, "user@test.com", "User", "USER");
	}

	// 캠프 생성 성공 테스트
	@Test
	void testCreateCamp_Success() {
		// Given: 필요한 데이터를 준비
		Manager mockManager = new Manager(1L, "admin@test.com", "encodedPassword", "Admin");
		CampRequestDto requestDto = new CampRequestDto(
			"Spring Camp",
			"Description",
			LocalDate.of(2024, 12, 5),
			LocalDate.of(2024, 12, 15),
			50
		);
		Camp mockCamp = Camp.builder()
			.id(1L)
			.name("Spring Camp")
			.contents("Description")
			.openDate(LocalDate.of(2024, 12, 5))
			.closeDate(LocalDate.of(2024, 12, 15))
			.maxCount(50)
			.remainCount(50)
			.status(CampStatus.CREATED)
			.manager(mockManager)
			.build();

		// Mock 동작 설정
		when(managerRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(mockManager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);
		when(campRepository.save(any())).thenReturn(mockCamp);

		// When: 실제 메서드 호출
		CampResponseDto responseDto = campService.createCamp(requestDto, adminUser);

		// Then: 결과 검증
		assertNotNull(responseDto); // 응답 객체가 null이 아닌지 확인
		assertEquals("Spring Camp", responseDto.getName()); // 캠프 이름 검증
		assertEquals(50, responseDto.getMaxCount()); // 최대 인원 검증
		verify(campRepository, times(1)).save(any(Camp.class)); // 캠프 저장 메서드가 호출되었는지 확인
	}

	// 매니저를 찾지 못한 경우의 테스트
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

		// Mock 동작 설정
		when(managerRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.empty());

		// When & Then: 예외 발생 여부 검증
		ManagerException exception = assertThrows(
			ManagerException.class,
			() -> campService.createCamp(requestDto, adminUser)
		);
		assertEquals(ExceptionCode.NOT_FOUND_MANAGER, exception.getExceptionCode());
	}

	// 캠프 단건 조회 성공 테스트
	@Test
	void testGetCampById_Success() {
		// Given
		Manager manager = new Manager(1L, "admin@test.com", "encodedPassword", "Admin");
		Camp mockCamp = Camp.builder()
			.id(1L)
			.name("Spring Camp")
			.contents("Description")
			.openDate(LocalDate.of(2024, 12, 5))
			.closeDate(LocalDate.of(2024, 12, 15))
			.maxCount(50)
			.remainCount(50)
			.status(CampStatus.CREATED)
			.manager(manager)
			.build();

		// Mock 동작 설정
		when(campRepository.findById(1L)).thenReturn(Optional.of(mockCamp));

		// When
		CampResponseDto responseDto = campService.getCampById(1L);

		// Then
		assertNotNull(responseDto);
		assertEquals(1L, responseDto.getId());
		assertEquals("Spring Camp", responseDto.getName());
		verify(campRepository, times(1)).findById(1L);
	}

	// 캠프 리스트 조회 성공 테스트
	@Test
	void testGetAllCamps_Success() {
		// Given
		Manager manager = new Manager(1L, "admin@test.com", "encodedPassword", "Admin");
		Camp camp1 = Camp.builder()
			.id(1L)
			.name("Spring Camp")
			.contents("Description")
			.openDate(LocalDate.of(2024, 12, 5))
			.closeDate(LocalDate.of(2024, 12, 15))
			.maxCount(50)
			.remainCount(50)
			.status(CampStatus.RECRUITING)
			.manager(manager)
			.build();

		Camp camp2 = Camp.builder()
			.id(2L)
			.name("Java Camp")
			.contents("Learn Java")
			.openDate(LocalDate.of(2024, 12, 10))
			.closeDate(LocalDate.of(2024, 12, 20))
			.maxCount(30)
			.remainCount(20)
			.status(CampStatus.CREATED)
			.manager(manager)
			.build();

		// Mock 동작 설정
		when(campRepository.findAllOrderedByStatus()).thenReturn(List.of(camp1, camp2));

		// When
		List<CampResponseDto> responseDtos = campService.getAllCamps();

		// Then
		assertEquals(2, responseDtos.size());
		assertEquals("Spring Camp", responseDtos.get(0).getName());
		assertEquals("Java Camp", responseDtos.get(1).getName());
		verify(campRepository, times(1)).findAllOrderedByStatus();
	}

	// // 캠프 신청 성공 테스트
	// @Test
	// void testApplyForCamp_Success() {
	// 	// Given
	// 	Camp mockCamp = Camp.builder()
	// 		.id(1L)
	// 		.name("Spring Camp")
	// 		.contents("Description")
	// 		.openDate(LocalDate.of(2024, 12, 5))
	// 		.closeDate(LocalDate.of(2024, 12, 15))
	// 		.status(CampStatus.RECRUITING)
	// 		.maxCount(50)
	// 		.remainCount(10)
	// 		.build();
	//
	// 	User mockUser = User.builder()
	// 		.email("user@test.com")
	// 		.password("encodedPassword")
	// 		.username("User")
	// 		.build();
	// 	ReflectionTestUtils.setField(mockUser, "id", 2L);
	//
	// 	when(entityManager.find(eq(Camp.class), eq(1L), eq(LockModeType.PESSIMISTIC_WRITE), any(Map.class)))
	// 		.thenReturn(mockCamp); // Mock EntityManager 동작 설정
	// 	when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
	// 	when(userCampRepository.findByUserId(2L)).thenReturn(null);
	// 	when(userCampRepository.save(any(UserCamp.class))).thenAnswer(invocation -> invocation.getArgument(0));
	//
	// 	// When
	// 	UserCamp result = campService.applyForCamp(1L, new AuthUser(2L, "user@test.com", "User", "USER"));
	//
	// 	// Then
	// 	assertNotNull(result);
	// 	assertEquals(mockCamp.getId(), result.getCamp().getId());
	// 	assertEquals(mockUser.getId(), result.getUser().getId());
	// 	verify(entityManager, times(1)).find(eq(Camp.class), eq(1L), eq(LockModeType.PESSIMISTIC_WRITE), any(Map.class));
	// 	verify(userCampRepository, times(1)).save(any(UserCamp.class));
	// }
	//
	// // 캠프 신청 실패 테스트 (정원 초과)
	// @Test
	// void testApplyForCamp_FullCapacity() {
	// 	// Given
	// 	Camp mockCamp = Camp.builder()
	// 		.id(1L)
	// 		.name("Spring Camp")
	// 		.contents("Description")
	// 		.openDate(LocalDate.of(2024, 12, 5))
	// 		.closeDate(LocalDate.of(2024, 12, 15))
	// 		.status(CampStatus.RECRUITING)
	// 		.maxCount(50)
	// 		.remainCount(0) // 정원 초과 상태
	// 		.build();
	//
	// 	// EntityManager Mock 설정
	// 	when(entityManager.find(eq(Camp.class), eq(1L), eq(LockModeType.PESSIMISTIC_WRITE), any(Map.class)))
	// 		.thenReturn(mockCamp);
	//
	// 	// When & Then
	// 	CampException exception = assertThrows(
	// 		CampException.class,
	// 		() -> campService.applyForCamp(1L, regularUser)
	// 	);
	//
	// 	assertEquals(ExceptionCode.EXCEEDED_CAMP_CAPACITY, exception.getExceptionCode()); // 예외 코드 확인
	// 	verify(entityManager, times(1)).find(eq(Camp.class), eq(1L), eq(LockModeType.PESSIMISTIC_WRITE), any(Map.class));
	// }
}
