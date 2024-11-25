package com.sparta.spartascheduling.domain.counsel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.counsel.dto.CounselRequest;
import com.sparta.spartascheduling.domain.counsel.dto.CounselResponse;
import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;
import com.sparta.spartascheduling.domain.counsel.repository.CounselRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;

@ExtendWith(MockitoExtension.class)
class CounselServiceTest {

	@InjectMocks
	private CounselService counselService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TutorRepository tutorRepository;

	@Mock
	private UserCampRepository userCampRepository;

	@Mock
	private CounselRepository counselRepository;

	@Test
	void createCounsel_Success() {
		// Given
		AuthUser authUser = new AuthUser(1L, "user1@gmail.com", "user1", "USER");
		CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.of(2024, 11, 25, 19, 30));
		Manager manager = Manager.builder()
			.username("manager")
			.email("manager@gmail.com")
			.password("1234")
			.build();
		Camp camp = new Camp(1L,
			"CampName",
			"camp1",
			LocalDate.of(2024, 1, 1),
			LocalDate.of(2024, 12, 30),
			CampStatus.CREATED,
			1000,
			manager,
			800);

		User mockUser = new User("user1@gmail.com", "1234", "user1");
		Tutor mockTutor = Tutor.builder()
			.id(2L)
			.counselStart(LocalTime.of(9, 0))
			.counselEnd(LocalTime.of(23, 0))
			.campId(1L)
			.build();
		UserCamp mockUserCamp = new UserCamp(mockUser, camp);
		Counsel mockCounsel = Counsel.builder()
			.user(mockUser)
			.tutor(mockTutor)
			.datetime(LocalDateTime.of(2024, 11, 25, 19, 30))
			.content(request.content())
			.status(CounselStatus.WAITING)
			.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
		when(tutorRepository.findById(2L)).thenReturn(Optional.of(mockTutor));
		when(userCampRepository.findById(1L)).thenReturn(Optional.of(mockUserCamp));
		when(counselRepository.findByUserIdAndStatus(1L, CounselStatus.WAITING)).thenReturn(Optional.empty());
		when(counselRepository.save(any(Counsel.class))).thenReturn(mockCounsel);

		// When
		CounselResponse response = counselService.createCounsel(authUser, request);

		// Then
		assertNotNull(response);
		assertEquals(request.content(), response.content());
		assertEquals(CounselStatus.WAITING, response.status());
	}

	@Test
	void createCounsel_UserTypeNotAllowed_ThrowsException() {
		// Given
		AuthUser authUser = new AuthUser(1L, "tutor@gmail.com", "user1", "TUTOR");
		CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.of(2024, 11, 25, 19, 30));

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	}

	@Test
	void createCounsel_TutorNotFound_ThrowsException() {
		// Given
		AuthUser authUser = new AuthUser(1L, "user1@gmail.com", "user1", "USER");
		CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.of(2024, 11, 25, 19, 30));

		User mockUser = new User("user1@gmail.com", "1234", "user1");

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
		when(tutorRepository.findById(2L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	}

	@Test
	void createCounsel_ExistingCounsel_ThrowsException() {
		// Given
		AuthUser authUser = new AuthUser(1L, "user1@gmail.com", "user1", "USER");
		CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.of(2024, 11, 25, 19, 30));

		User mockUser = new User("user1@gmail.com", "1234", "user1");
		Tutor mockTutor = Tutor.builder()
			.id(2L)
			.counselStart(LocalTime.of(9, 0))
			.counselEnd(LocalTime.of(23, 0))
			.campId(1L)
			.build();
		Counsel existingCounsel = new Counsel();

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
		when(tutorRepository.findById(2L)).thenReturn(Optional.of(mockTutor));
		when(counselRepository.findByUserIdAndStatus(1L, CounselStatus.WAITING)).thenReturn(Optional.of(existingCounsel));

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	}

	@Test
	void createCounsel_RequestBeforeNow_ThrowsException() {
		// Given
		AuthUser authUser = new AuthUser(1L, "user1@gmail.com", "user1", "USER");
		CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.of(2024, 11, 25, 19, 30));

		User mockUser = new User("user1@gmail.com", "1234", "user1");

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	}

	@Test
	void createCounsel_RequestOutsideCounselHours_ThrowsException() {
		// Given
		AuthUser authUser = new AuthUser(1L, "user1@gmail.com", "user1", "USER");
		CounselRequest request = new CounselRequest(2L, "Request Content", LocalDateTime.of(2024, 11, 25, 19, 30));

		User mockUser = new User("user1@gmail.com", "1234", "user1");
		Tutor mockTutor = Tutor.builder()
			.id(2L)
			.counselStart(LocalTime.of(9, 0))
			.counselEnd(LocalTime.of(18, 0))
			.campId(1L)
			.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
		when(tutorRepository.findById(2L)).thenReturn(Optional.of(mockTutor));

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> counselService.createCounsel(authUser, request));
	}
}
