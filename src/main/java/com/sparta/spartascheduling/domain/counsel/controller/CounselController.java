package com.sparta.spartascheduling.domain.counsel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartascheduling.common.annotation.Auth;
import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.counsel.dto.CounselRequest;
import com.sparta.spartascheduling.domain.counsel.dto.CounselResponse;
import com.sparta.spartascheduling.domain.counsel.service.CounselService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;

	@PostMapping("/counsels")
	public ResponseEntity<CounselResponse> createCounsel(@Auth AuthUser authUser, @Valid @RequestBody CounselRequest request) {
		CounselResponse response = counselService.createCounsel(authUser, request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/tutor/counsels")
	public ResponseEntity<List<CounselResponse>> getCounselFromTutor(@Auth AuthUser authUser) {
		List<CounselResponse> response = counselService.getCounselFromTutor(authUser);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
