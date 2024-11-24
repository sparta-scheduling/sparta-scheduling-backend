package com.sparta.spartascheduling.domain.camp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.service.CampService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CampController {

	private final CampService campService;

	// 캠프 생성 API
	@PostMapping("/admin/camps")
	public ResponseEntity<CampResponseDto> createCamp(
		@RequestBody CampRequestDto requestDto,
		@RequestParam Long managerId) { // 추후 수정 예정
		CampResponseDto responseDto = campService.createCamp(requestDto, managerId);
		return ResponseEntity.ok(responseDto);
	}

	// 캠프 신청 - 동시성 제어 할 곳
	@PostMapping("/camps/{campId}")
	public void applyForCamp(@PathVariable Long campId) {
		campService.applyForCamp(campId);
	}
}
