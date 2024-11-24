package com.sparta.spartascheduling.domain.manager.controller;

import com.sparta.spartascheduling.domain.manager.dto.ManagerRequestDto;
import com.sparta.spartascheduling.domain.manager.service.ManagerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManagerController {

	private final ManagerService managerService;

	// 매니저 회원 가입 API
	@PostMapping("/manager/signup")
	public ResponseEntity<String> signupManager(@RequestBody ManagerRequestDto requestDto) {
		managerService.signupManager(requestDto);
		return ResponseEntity.ok("매니저 회원 가입이 완료되었습니다.");
	}
}
