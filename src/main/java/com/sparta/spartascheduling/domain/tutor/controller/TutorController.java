package com.sparta.spartascheduling.domain.tutor.controller;
import com.sparta.spartascheduling.common.annotation.Auth;
import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.tutor.dto.TutorMypageDto;
import com.sparta.spartascheduling.domain.tutor.dto.TutorRequest;
import com.sparta.spartascheduling.domain.tutor.dto.TutorResponseDto;
import com.sparta.spartascheduling.domain.tutor.service.TutorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    // 나의 캠프(튜터)
    @GetMapping("/tutor/mypage")
    public ResponseEntity<TutorMypageDto> getTutorMypage(@Auth AuthUser authUser){
        return ResponseEntity.ok(tutorService.getTutorMypage(authUser));
    }

    @PutMapping("/tutor/update-time")
    public ResponseEntity<TutorResponseDto> updateTime(@Auth AuthUser authUser, @Valid TutorRequest request) {
        TutorResponseDto response = tutorService.updateTime(authUser, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
