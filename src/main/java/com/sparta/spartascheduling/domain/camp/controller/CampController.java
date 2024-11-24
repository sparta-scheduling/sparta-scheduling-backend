package com.sparta.spartascheduling.domain.camp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.service.CampService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/camps")
@RequiredArgsConstructor
public class CampController {

	private final CampService campService;

	// 캠프 생성 API
	@PostMapping
	public ResponseEntity<CampResponseDto> createCamp(
		@RequestBody CampRequestDto requestDto,
		HttpServletRequest request) {
		Long managerId = (Long)request.getAttribute("userId");
		CampResponseDto responseDto = campService.createCamp(requestDto, managerId);
		return ResponseEntity.ok(responseDto);
	}
}
