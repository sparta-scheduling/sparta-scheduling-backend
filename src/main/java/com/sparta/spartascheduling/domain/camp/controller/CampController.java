package com.sparta.spartascheduling.domain.camp.controller;
import com.sparta.spartascheduling.domain.camp.service.CampService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 
    // 캠프 신청 - 동시성 제어 할 곳
    @PostMapping("/camps/{campId}")
    public void applyForCamp(@PathVariable Long campId){
         campService.applyForCamp(campId);
    }
}
