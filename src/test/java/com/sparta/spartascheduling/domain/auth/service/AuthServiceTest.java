package com.sparta.spartascheduling.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sparta.spartascheduling.common.config.PasswordEncoder;
import com.sparta.spartascheduling.domain.auth.dto.request.SignupRequestDto;
import com.sparta.spartascheduling.domain.auth.dto.response.SignupResponseDto;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;

class AuthServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private ManagerRepository managerRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthService authService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("회원가입 성공 테스트")
	void signupSuccess() {
		// given
		SignupRequestDto requestDto = new SignupRequestDto(
			"TestUser",
			"test@example.com",
			"password123",
			"password123",
			false
		);

		when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
		User savedUser = User.builder()
			.email(requestDto.getEmail())
			.password("encodedPassword")
			.username(requestDto.getUsername())
			.build();
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		// When
		SignupResponseDto responseDto = authService.signup(requestDto);

		// Then
		assertNotNull(responseDto);
		assertEquals("test@example.com", responseDto.getEmail());
		assertEquals("TestUser", responseDto.getUsername());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("중복된 이메일로 회원가입 시도 테스트")
	void signupDuplicateEmail() {
		// Given
		SignupRequestDto requestDto = new SignupRequestDto(
			"DuplicateUser",
			"duplicate@exmple.com",
			"password123",
			"password123",
			false
		);

		when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

		// When
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> authService.signup(requestDto)
		);

		// Then
		assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	@DisplayName("비밀번호 확인 실패 테스트")
	void signupPasswordMismatch() {
		// Given
		SignupRequestDto requestDto = new SignupRequestDto(
			"TestUser",
			"test@example.com",
			"wrongpassword",
			"password123",
			false
		);

		// When
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> authService.signup(requestDto)
		);

		// Then
		assertEquals("비밀번호를 동일하게 입력해 주십시오.", exception.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}
}