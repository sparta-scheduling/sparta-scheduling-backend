package com.sparta.spartascheduling.domain.manager.service;

import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.domain.manager.dto.ManagerRequestDto;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerService {

	private final ManagerRepository managerRepository;

	public void signupManager(ManagerRequestDto requestDto) {
	}
}
