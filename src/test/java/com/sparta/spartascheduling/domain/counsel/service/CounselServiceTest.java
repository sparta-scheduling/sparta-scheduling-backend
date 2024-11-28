package com.sparta.spartascheduling.domain.counsel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.counsel.dto.CounselRequest;
import com.sparta.spartascheduling.domain.counsel.dto.CounselResponse;
import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;
import com.sparta.spartascheduling.domain.counsel.repository.CounselRepository;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class CounselServiceTest {

	private CounselService counselService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TutorRepository tutorRepository;

	@Autowired
	private UserCampRepository userCampRepository;

	@Autowired
	private CounselRepository counselRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach

	void setUp() {

		counselService = new CounselService(counselRepository, userRepository, tutorRepository, userCampRepository);
	}
	// @Test
	// void createCounsel_Success() {
	// 	// Given
	// 	AuthUser authUser = new AuthUser(1L, "user1@gmail.com","user1","USER");
	// 	CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.now().plusHours(1));
	//
	// 	User mockUser = new User("user1@gmail.com", "Student","user1");
	// 	Tutor mockTutor = Tutor.builder()
	// 		.id(2L)
	// 		.counselStart(LocalTime.of(9, 0))
	// 		.counselEnd(LocalTime.of(18, 0))
	// 		.campId(1L)
	// 		.build();
	// 	UserCamp mockUserCamp = new UserCamp(1L, mockUser, new Camp(1L, "CampName", "camp1", "3","9"));
	// 	Counsel mockCounsel = Counsel.builder()
	// 		.user(mockUser)
	// 		.tutor(mockTutor)
	// 		.datetime(LocalDateTime.now().plusHours(1))
	// 		.content(request.content())
	// 		.status(CounselStatus.WAITING)
	// 		.build();
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
	// 	when(tutorRepository.findById(2L)).thenReturn(Optional.of(mockTutor));
	// 	when(userCampRepository.findById(1L)).thenReturn(Optional.of(mockUserCamp));
	// 	when(counselRepository.findByUserIdAndStatus(1L, CounselStatus.WAITING)).thenReturn(Optional.empty());
	// 	when(counselRepository.save(any(Counsel.class))).thenReturn(mockCounsel);
	//
	// 	// When
	// 	CounselResponse response = counselService.createCounsel(authUser, request);
	//
	// 	// Then
	// 	assertNotNull(response);
	// 	assertEquals(request.content(), response.getContent());
	// 	assertEquals(CounselStatus.WAITING, response.getStatus());
	// }
	//
	// @Test
	// void createCounsel_UserTypeNotAllowed_ThrowsException() {
	// 	// Given
	// 	AuthUser authUser = new AuthUser(1L, "TUTOR");
	// 	CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.now().plusHours(1));
	//
	// 	// When & Then
	// 	assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	// }
	//
	// @Test
	// void createCounsel_TutorNotFound_ThrowsException() {
	// 	// Given
	// 	AuthUser authUser = new AuthUser(1L, "USER");
	// 	CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.now().plusHours(1));
	//
	// 	User mockUser = new User(1L, "Student");
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
	// 	when(tutorRepository.findById(2L)).thenReturn(Optional.empty());
	//
	// 	// When & Then
	// 	assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	// }
	//
	// @Test
	// void createCounsel_ExistingCounsel_ThrowsException() {
	// 	// Given
	// 	AuthUser authUser = new AuthUser(1L, "USER");
	// 	CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.now().plusHours(1));
	//
	// 	User mockUser = new User(1L, "Student");
	// 	Tutor mockTutor = Tutor.builder()
	// 		.id(2L)
	// 		.counselStart(LocalTime.of(9, 0))
	// 		.counselEnd(LocalTime.of(18, 0))
	// 		.campId(1L)
	// 		.build();
	// 	Counsel existingCounsel = new Counsel();
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
	// 	when(tutorRepository.findById(2L)).thenReturn(Optional.of(mockTutor));
	// 	when(counselRepository.findByUserIdAndStatus(1L, CounselStatus.WAITING)).thenReturn(Optional.of(existingCounsel));
	//
	// 	// When & Then
	// 	assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	// }
	//
	// @Test
	// void createCounsel_RequestBeforeNow_ThrowsException() {
	// 	// Given
	// 	AuthUser authUser = new AuthUser(1L, "USER");
	// 	CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.now().minusHours(1));
	//
	// 	User mockUser = new User(1L, "Student");
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
	//
	// 	// When & Then
	// 	assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	// }
	//
	// @Test
	// void createCounsel_RequestOutsideCounselHours_ThrowsException() {
	// 	// Given
	// 	AuthUser authUser = new AuthUser(1L, "USER");
	// 	CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.now().withHour(20).withMinute(0));
	//
	// 	User mockUser = new User(1L, "Student");
	// 	Tutor mockTutor = Tutor.builder()
	// 		.id(2L)
	// 		.counselStart(LocalTime.of(9, 0))
	// 		.counselEnd(LocalTime.of(18, 0))
	// 		.campId(1L)
	// 		.build();
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
	// 	when(tutorRepository.findById(2L)).thenReturn(Optional.of(mockTutor));
	//
	// 	// When & Then
	// 	assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	// }

	@Test
	@DisplayName("낙관적 락 충돌 발생 테스트")
	void testOptimisticLockingConflict() throws InterruptedException {
		// Given: 초기 상담 데이터를 생성
		User user1 = userRepository.save(new User("user1@gmail.com", "Student","user1"));
		User user2 = userRepository.save(new User("user2@gmail.com", "Student","user2"));

		Tutor tutor = Tutor.builder()
				.counselStart(LocalTime.of(9, 0))
				.counselEnd(LocalTime.of(18, 0))
				.campId(1L)
				.build();
		tutorRepository.save(tutor);

		CounselRequest request = new CounselRequest(
			tutor.getId(),
			"상담내용",
			LocalDateTime.of(2024,11,29,13,00)
		);

		AuthUser authUser1 = new AuthUser(user1.getId(), user1.getEmail(),user1.getUsername(),"USER");
		AuthUser authUser2 = new AuthUser(user2.getId(), user2.getEmail(),user2.getUsername(),"USER");
		entityManager.flush();
		// 동시성을 시뮬레이션하기 위한 두 개의 쓰레드 생성
		Runnable task1 = () -> {
				counselService.createCounsel(authUser1, request);
		};

		Runnable task2 = () -> {

				counselService.createCounsel(authUser2, request);

		};

		// 두 쓰레드를 동시에 실행
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.execute(task1);
		executorService.execute(task2);

		// 모든 쓰레드가 끝날 때까지 대기
		executorService.shutdown();
		executorService.awaitTermination(5, TimeUnit.SECONDS);

		// Then: 결과 검증
		List<Counsel> counsels = counselRepository.findAll();
		assertEquals(1, counsels.size(), "하나의 상담만 생성되어야 함");
	}

}
