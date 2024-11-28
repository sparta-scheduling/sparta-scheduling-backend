package com.sparta.spartascheduling.domain.camp.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
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

	private Camp tCamp;


	@BeforeEach
	void setUp() {
		Manager manager = Manager.builder()
			.email("admin@test.com")
			.username("admin1")
			.password("password")
			.build();

		managerRepository.save(manager);

		tCamp = Camp.builder()
			.name("Java/Spring 5기")
			.contents("contents1")
			.openDate(LocalDate.of(2024, 11, 30))
			.closeDate(LocalDate.of(2025, 11, 11))
			.manager(manager)
			.remainCount(200)
			.build();

		campRepository.save(tCamp);

		for (int i = 0; i < 100; i++) {
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

		for (int i = 0; i < 1000; i++) {
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
		assertThat(result.getRemainCount()).isEqualTo(1000);
	}

	@Test
	@DisplayName("동시에 100명이 수강신청 진행, 그리고 Redisson")
	void test2() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch countDownLatch = new CountDownLatch(100);

		for (int i = 0; i < 100; i++) {
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
		assertThat(result.getRemainCount()).isEqualTo(100);
	}

}