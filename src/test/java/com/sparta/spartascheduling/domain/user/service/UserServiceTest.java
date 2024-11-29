package com.sparta.spartascheduling.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.enums.DeleteStatus;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;

class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserCampRepository userCampRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("권한이 없는 유저가 삭제를 요청한 경우")
	public void deleteUser_NoPermission_ThrowsException() {
		// Given
		AuthUser authUser = new AuthUser(2L, "abc@test.com", "user1", "USER");
		Long userId = 1L;

		// When & Then
		assertThrows(IllegalArgumentException.class,
			() -> userService.deleteUser(authUser, userId),
			"권한이 없습니다.");
	}

	@Test
	@DisplayName("이미 삭제된 유저를 삭제 요청")
	public void deleteUser_UserAlreadyInactive_ThrowsException() {
		// 추후 수정
	}
}