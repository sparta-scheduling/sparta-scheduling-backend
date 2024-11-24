package com.sparta.spartascheduling.domain.manager.service;

import com.sparta.spartascheduling.domain.manager.dto.ManagerRequestDto;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {

	private final ManagerRepository managerRepository;

	// 매니저 회원 가입 로직
	public void signupManager(ManagerRequestDto requestDto) {
		// 이메일 중복 확인
		if (managerRepository.existsByEmail(requestDto.getEmail())) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		// 매니저 생성 및 저장
		Manager manager = Manager.createManager(
			requestDto.getEmail(),
			requestDto.getPassword(),
			requestDto.getUsername()
		);

		managerRepository.save(manager);
	}
}
