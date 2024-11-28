package com.sparta.spartascheduling.domain.camp.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.camp.service.lettuce.LettuceLockFacade;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;

@SpringBootTest
	// @DataJpaTest
	// @Import({CampService.class, CampLockFacade.class})
class CampServiceConcurrentTest {

	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private CampRepository campRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CampLockFacade campLockFacade;
	@Autowired
	private CampService campService;

	@Autowired
	private LettuceLockFacade lettuceLockFacade;

	private Camp tCamp;

	private static int USER_COUNT = 300;


	@BeforeEach
	void setUp() {
		Manager manager = Manager.builder()
			.email("admin@test.com")
			.username("admin1")
			.password("password")
			.build();

		managerRepository.save(manager);

		CampRequestDto requestDto = new CampRequestDto(
			"Java/Spring 3기",
			"contents1",
			LocalDate.of(2024, 11, 29),
			LocalDate.of(2025, 11, 11),
			140
		);

		tCamp = Camp.createCamp(
			requestDto.getName(),
			requestDto.getContents(),
			requestDto.getOpenDate(),
			requestDto.getCloseDate(),
			requestDto.getMaxCount(),
			manager
		);

		campRepository.save(tCamp);

		for (int i = 0; i < USER_COUNT; i++) {
			User user = User.builder()
				.email("user" + i + "@test.com")
				.username("user" + i)
				.password("password")
				.build();

			userRepository.save(user);
		}
	}

	@Test
	@DisplayName("동시에 100명이 수강신청 진행")
	void test1() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(1000);
		CountDownLatch countDownLatch = new CountDownLatch(1000);

		for (int i = 0; i < USER_COUNT; i++) {
			AuthUser authUser = new AuthUser((long) i, "user" + i + "@test.com", "user" + i, "USER");
			executorService.submit(() -> {
				try {
					campService.applyForCamp(tCamp.getId(), authUser);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		Camp result = campRepository.findById(tCamp.getId()).orElseThrow();
		assertThat(result.getRemainCount()).isNotEqualTo(0);
	}

	@Test
	@DisplayName("동시에 100명이 수강신청 진행, 그리고 비관적 락")
	void test2() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(USER_COUNT);
		CountDownLatch countDownLatch = new CountDownLatch(USER_COUNT);

		for (int i = 0; i < USER_COUNT; i++) {
			AuthUser authUser = new AuthUser((long) i, "user" + i + "@test.com", "user" + i, "USER");
			executorService.submit(() -> {
				try {
					campService.applyForCampPessimistic(tCamp.getId(), authUser);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		Camp result = campRepository.findById(tCamp.getId()).orElseThrow();
		assertThat(result.getRemainCount()).isEqualTo(0);
	}

	@Test
	@DisplayName("동시에 100명이 수강신청 진행, 그리고 Redisson")
	void test3() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(USER_COUNT);
		CountDownLatch countDownLatch = new CountDownLatch(USER_COUNT);

		for (int i = 0; i < USER_COUNT; i++) {
			AuthUser authUser = new AuthUser((long) i, "user" + i + "@test.com", "user" + i, "USER");
			executorService.submit(() -> {
				try {
					campLockFacade.applyForCampRedisson(tCamp.getId(), authUser);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		Camp result = campRepository.findById(tCamp.getId()).orElseThrow();
		assertThat(result.getRemainCount()).isEqualTo(0);
	}

	@Test
	@DisplayName("동시에 100명이 수강신청 진행, 그리고 lettuce")
	void test3() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch countDownLatch = new CountDownLatch(100);

		for (int i = 1; i <= 100; i++) {
			AuthUser authUser = new AuthUser((long)i, "user" + i + "@test.com", "user" + i, "USER");
			executorService.submit(() -> {
				try {
					campService.applyForCampLettuce(tCamp.getId(), authUser);
				} finally {

					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		executorService.shutdown();

		Camp result = campRepository.findById(tCamp.getId()).orElseThrow();
		assertThat(result.getRemainCount()).isEqualTo(0);
	}

}