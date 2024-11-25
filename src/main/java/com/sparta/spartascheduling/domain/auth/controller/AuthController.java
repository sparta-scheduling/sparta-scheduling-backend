package com.sparta.spartascheduling.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartascheduling.domain.auth.dto.request.SigninRequestDto;
import com.sparta.spartascheduling.domain.auth.dto.request.SignupRequestDto;
import com.sparta.spartascheduling.domain.auth.dto.response.SignupResponseDto;
import com.sparta.spartascheduling.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public SignupResponseDto signup(
        @Valid @RequestBody SignupRequestDto signupRequestDto
    ) {
        return authService.signup(signupRequestDto);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Void> signin(
        @Valid @RequestBody SigninRequestDto signinRequestDto
    ) {
        authService.signin(signinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
