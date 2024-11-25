package com.sparta.spartascheduling.domain.camp.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartascheduling.domain.camp.service.CampService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CampController {

	private final CampService campService;

	@PostMapping("/admin/camps")
	public ResponseEntity<CampResponseDto> createCamp(
		@RequestBody CampRequestDto requestDto,
		@Auth AuthUser authUser
	) {
		CampResponseDto responseDto = campService.createCamp(requestDto, authUser);
		return ResponseEntity.ok(responseDto);
	}

	// 캠프 신청 - 동시성 제어 할 곳
	@PostMapping("/camps/{campId}")
	public void applyForCamp(@PathVariable Long campId) {
		campService.applyForCamp(campId);
	}
}
