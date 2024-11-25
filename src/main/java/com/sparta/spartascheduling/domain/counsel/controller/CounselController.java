package com.sparta.spartascheduling.domain.counsel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartascheduling.domain.counsel.dto.CounselRequest;
import com.sparta.spartascheduling.domain.counsel.dto.CreateCounselResponse;
import com.sparta.spartascheduling.domain.counsel.service.CounselService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;

	@PostMapping("/counsels")
	public ResponseEntity<CreateCounselResponse> createCounsel(@AuthenticationUserId Long id, CounselRequest request) {
		CreateCounselResponse response = counselService.createCounsel(id, request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
