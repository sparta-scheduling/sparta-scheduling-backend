// package com.sparta.spartascheduling.domain.counsel.service;
//
// import java.time.LocalDateTime;
// import java.util.Optional;
//
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import com.sparta.spartascheduling.domain.counsel.dto.CounselRequest;
// import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
// import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;
// import com.sparta.spartascheduling.domain.counsel.repository.CounselRepository;
// import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
// import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
// import com.sparta.spartascheduling.domain.user.entity.User;
// import com.sparta.spartascheduling.domain.user.repository.UserRepository;
// import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
//
// @SpringBootTest
// @ExtendWith(MockitoExtension.class)
// public class CounselServiceTest {
//
// 	@Mock
// 	private CounselRepository counselRepository;
//
// 	@Mock
// 	private UserRepository userRepository;
//
// 	@Mock
// 	private TutorRepository tutorRepository;
//
// 	@Mock
// 	private UserCampRepository userCampRepository;
//
// 	@InjectMocks
// 	private CounselService counselService;
//
// 	@Test
// 	void createCounsel_shouldThrowException_whenUserNotFound() {
// 		// Given
// 		Long userId = 1L;
// 		CounselRequest request = new CounselRequest(1L, "Test Content", LocalDateTime.now());
//
// 		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
// 		// When & Then
// 		Assertions.assertThrows(IllegalArgumentException.class,
// 			() -> counselService.createCounsel(userId, request),
// 			"유저를 찾을 수 없습니다."
// 		);
// 	}
//
// 	@Test
// 	void createCounsel_shouldThrowException_whenTutorNotFound() {
// 		// Given
// 		Long userId = 1L;
// 		Long tutorId = 2L;
// 		CounselRequest request = new CounselRequest(tutorId, "Test Content", LocalDateTime.now());
//
// 		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
// 		Mockito.when(tutorRepository.findById(tutorId)).thenReturn(Optional.empty());
//
// 		// When & Then
// 		Assertions.assertThrows(IllegalArgumentException.class,
// 			() -> counselService.createCounsel(userId, request),
// 			"튜터를 찾을 수 없습니다."
// 		);
// 	}
//
// 	@Test
// 	void createCounsel_shouldThrowException_whenExistingCounselIsWaiting() {
// 		// Given
// 		Long userId = 1L;
// 		Long tutorId = 2L;
// 		CounselRequest request = new CounselRequest(tutorId, "Test Content", LocalDateTime.now());
// 		User user = new User();
// 		Tutor tutor = new Tutor();
//
// 		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// 		Mockito.when(tutorRepository.findById(tutorId)).thenReturn(Optional.of(tutor));
// 		Mockito.when(counselRepository.findByUserIdAndStatus(userId, CounselStatus.WAITING))
// 			.thenReturn(Optional.of(new Counsel()));
//
// 		// When & Then
// 		Assertions.assertThrows(IllegalArgumentException.class,
// 			() -> counselService.createCounsel(userId, request),
// 			"이미 진행 중인 상담이 있습니다. 새로운 상담을 신청할 수 없습니다."
// 		);
// 	}
//
// 	@Test
// 	void createCounsel_shouldThrowException_whenRequestTimeIsPast() {
// 		// Given
// 		Long userId = 1L;
// 		Long tutorId = 2L;
// 		LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
// 		CounselRequest request = new CounselRequest(tutorId, "Test Content", pastTime);
// 		User user = new User();
// 		Tutor tutor = new Tutor();
//
// 		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// 		Mockito.when(tutorRepository.findById(tutorId)).thenReturn(Optional.of(tutor));
// 		Mockito.when(counselRepository.findByUserIdAndStatus(userId, CounselStatus.WAITING))
// 			.thenReturn(Optional.empty());
//
// 		// When & Then
// 		Assertions.assertThrows(IllegalArgumentException.class,
// 			() -> counselService.createCounsel(userId, request),
// 			"상담을 신청할 수 없는 날짜입니다."
// 		);
// 	}
//
// 	// @Test
// 	// void createCounsel_shouldThrowException_whenRequestTimeIsOutsideAvailableHours() {
// 	// 	// Given
// 	// 	Long userId = 1L;
// 	// 	Long tutorId = 2L;
// 	// 	LocalDateTime requestTime = LocalDateTime.now().withHour(23); // 비정상 시간
// 	// 	CounselRequest request = new CounselRequest(tutorId, "Test Content", requestTime);
// 	// 	User user = new User();
// 	// 	Tutor tutor = new Tutor();
// 	// 	tutor.setCounselStart(LocalTime.of(9, 0));
// 	// 	tutor.setCounselEnd(LocalTime.of(18, 0));
// 	//
// 	// 	Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// 	// 	Mockito.when(tutorRepository.findById(tutorId)).thenReturn(Optional.of(tutor));
// 	// 	Mockito.when(counselRepository.findByUserIdAndStatus(userId, CounselStatus.WAITING))
// 	// 		.thenReturn(Optional.empty());
// 	//
// 	// 	// When & Then
// 	// 	Assertions.assertThrows(IllegalArgumentException.class,
// 	// 		() -> counselService.createCounsel(userId, request),
// 	// 		"상담 시간이 아닙니다. 상담 가능 시간: 09:00 ~ 18:00"
// 	// 	);
// 	// }
// 	//
// 	// @Test
// 	// void createCounsel_shouldCreateCounselSuccessfully() {
// 	// 	// Given
// 	// 	Long userId = 1L;
// 	// 	Long tutorId = 2L;
// 	// 	LocalDateTime validTime = LocalDateTime.now().plusHours(1);
// 	// 	CounselRequest request = new CounselRequest(tutorId, validTime, "Test Content");
// 	// 	User user = new User();
// 	// 	Tutor tutor = new Tutor();
// 	// 	tutor.setCounselStart(LocalTime.of(9, 0));
// 	// 	tutor.setCounselEnd(LocalTime.of(18, 0));
// 	//
// 	// 	Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// 	// 	Mockito.when(tutorRepository.findById(tutorId)).thenReturn(Optional.of(tutor));
// 	// 	Mockito.when(counselRepository.findByUserIdAndStatus(userId, CounselStatus.WAITING))
// 	// 		.thenReturn(Optional.empty());
// 	//
// 	// 	Mockito.when(counselRepository.save(Mockito.any(Counsel.class)))
// 	// 		.thenAnswer(invocation -> invocation.getArgument(0));
// 	//
// 	// 	// When
// 	// 	CounselResponse response = counselService.createCounsel(userId, request);
// 	//
// 	// 	// Then
// 	// 	Assertions.assertNotNull(response);
// 	// 	Assertions.assertEquals(CounselStatus.WAITING, response.status());
// 	// 	Assertions.assertEquals(tutorId, response.tutorId());
// 	// 	Assertions.assertEquals(userId, response.userId());
// 	// 	Assertions.assertEquals("Test Content", response.content());
// 	// }
// }
