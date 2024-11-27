package com.sparta.spartascheduling.domain.camp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.sparta.spartascheduling.common.util.RedisLockUtil;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.customException.ManagerException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CampServiceTest {

	@Mock
	private CampRepository campRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserCampRepository userCampRepository;

	@Mock
	private ManagerRepository managerRepository;

	@Mock
	private RedisLockUtil redisLockUtil;

	@InjectMocks
	private CampService campService;

	private AuthUser adminUser;
	private AuthUser regularUser;
	private Camp testCamp;

	@BeforeEach
	void setup() {
		adminUser = new AuthUser(1L, "admin@test.com", "Admin", "ADMIN");
		regularUser = new AuthUser(2L, "user@test.com", "User", "USER");

		testCamp = Camp.builder()
			.id(1L)
			.name("Spring Camp")
			.contents("Description")
			.openDate(LocalDate.of(2024, 12, 5))
			.closeDate(LocalDate.of(2024, 12, 15))
			.maxCount(50)
			.remainCount(50)
			.status(CampStatus.CREATED)
			.build();
	}

	// 캠프 생성 성공 테스트
	@Test
	void testCreateCamp_Success() {
		// Given
		Manager mockManager = new Manager(1L, "admin@test.com", "encodedPassword", "Admin");
		CampRequestDto requestDto = new CampRequestDto(
			"Spring Camp",
			"Description",
			LocalDate.of(2024, 12, 5),
			LocalDate.of(2024, 12, 15),
			50
		);

		when(managerRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(mockManager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);
		when(campRepository.save(any())).thenReturn(testCamp);

		// When
		CampResponseDto responseDto = campService.createCamp(requestDto, adminUser);

		// Then
		assertNotNull(responseDto);
		assertEquals("Spring Camp", responseDto.getName());
		assertEquals(50, responseDto.getMaxCount());
		verify(campRepository, times(1)).save(any(Camp.class));
	}

	// 매니저가 없는 경우 테스트
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

	// 캠프 단건 조회 테스트
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
			.maxCount(5)
			.remainCount(5)
			.status(CampStatus.CREATED)
			.manager(manager)
			.build();

		when(campRepository.findById(1L)).thenReturn(Optional.of(mockCamp));

		// When
		CampResponseDto responseDto = campService.getCampById(1L);

		// Then
		assertNotNull(responseDto);
		assertEquals(1L, responseDto.getId());
		assertEquals("Spring Camp", responseDto.getName());
		verify(campRepository, times(1)).findById(1L);
	}

	// 동시성 제어 테스트
	@Test
	void testApplyForCamp_Concurrency() throws InterruptedException {
		// Given
		User mockUser = new User();
		ReflectionTestUtils.setField(mockUser, "id", 1L);
		ReflectionTestUtils.setField(mockUser, "email", "user@test.com");
		ReflectionTestUtils.setField(mockUser, "password", "encodedPassword");
		ReflectionTestUtils.setField(mockUser, "username", "User");

		when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(mockUser));
		when(campRepository.findById(testCamp.getId())).thenReturn(Optional.of(testCamp));

		// Redis 락 상태를 관리하는 Mock 설정
		Map<String, Boolean> lockState = new ConcurrentHashMap<>();
		when(redisLockUtil.acquireLock(anyString(), anyLong()))
			.thenAnswer(invocation -> {
				String key = invocation.getArgument(0, String.class);
				synchronized (lockState) {
					if (lockState.getOrDefault(key, false)) {
						return false; // 이미 락이 획득된 상태
					} else {
						lockState.put(key, true); // 락 획득
						return true;
					}
				}
			});

		doAnswer(invocation -> {
			String key = invocation.getArgument(0, String.class);
			synchronized (lockState) {
				lockState.remove(key); // 락 해제
			}
			return null;
		}).when(redisLockUtil).releaseLock(anyString());

		int numberOfThreads = 20;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failureCount = new AtomicInteger(0);

		// When
		for (int i = 0; i < numberOfThreads; i++) {
			executorService.submit(() -> {
				try {
					campService.applyForCamp(testCamp.getId(), regularUser);
					successCount.incrementAndGet();
				} catch (Exception e) {
					failureCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// Then
		assertEquals(30, testCamp.getRemainCount(), "The remaining camp count should be 30");
		assertEquals(10, failureCount.get(), "There should be 10 failures due to exceeded capacity");
		verify(redisLockUtil, times(numberOfThreads)).acquireLock(anyString(), anyLong());
		verify(redisLockUtil, times(numberOfThreads)).releaseLock(anyString());
	}
}
