package com.sparta.spartascheduling.domain.camp.controller;

import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.service.CampService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	// 캠프 단건 조회 API
	@GetMapping("/camps/{campId}")
	public ResponseEntity<CampResponseDto> getCampById(@PathVariable Long campId) {
		CampResponseDto responseDto = campService.getCampById(campId);
		return ResponseEntity.ok(responseDto);
	}
}
