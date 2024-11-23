package com.sparta.spartascheduling.domain.user.controller;

import com.sparta.spartascheduling.domain.user.dto.UserMypageDto;
import com.sparta.spartascheduling.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 나의 캠프(학생)
    @GetMapping("/mypage")
    public ResponseEntity<UserMypageDto> getMypage() { // 나중에 회원정보 받아서 처리
        return ResponseEntity.ok(userService.getMypage());
    }

}
