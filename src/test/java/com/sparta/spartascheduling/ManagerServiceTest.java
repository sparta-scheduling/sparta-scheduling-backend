package com.sparta.spartascheduling.domain.manager.service;

import com.sparta.spartascheduling.domain.manager.dto.ManagerRequestDto;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {

	@Mock
	private ManagerRepository managerRepository;

	@InjectMocks
	private ManagerService managerService;

	@Test
	public void 매니저회원가입_성공() {
		// given
		ManagerRequestDto requestDto = new ManagerRequestDto();
		requestDto.setEmail("manager@example.com");
		requestDto.setPassword("password123");
		requestDto.setUsername("매니저");

		when(managerRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
		when(managerRepository.save(any(Manager.class))).thenAnswer(invocation -> {
			Manager manager = invocation.getArgument(0);
			manager.setId(1L); // 임의의 ID 설정
			return manager;
		});

		// when
		managerService.signupManager(requestDto);

		// then
		verify(managerRepository, times(1)).save(any(Manager.class));
	}

	@Test
	public void 매니저회원가입_실패_이메일중복() {
		// given
		ManagerRequestDto requestDto = new ManagerRequestDto();
		requestDto.setEmail("manager@example.com");
		requestDto.setPassword("password123");
		requestDto.setUsername("매니저");

		when(managerRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			managerService.signupManager(requestDto);
		});

		assertEquals("이미 사용 중인 이메일입니다.", exception.getMessage());
		verify(managerRepository, never()).save(any(Manager.class));
	}
}
