package com.sparta.spartascheduling.domain.tutor.controller;
import com.sparta.spartascheduling.domain.tutor.dto.TotorMypageDto;
import com.sparta.spartascheduling.domain.tutor.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    // 나의 캠프(튜터)
    @GetMapping("/tutor/mypage")
    public ResponseEntity<TotorMypageDto> getTutorMypage(){ // 추후 로그인 정보 넣기
        return ResponseEntity.ok(tutorService.getTutorMypage());
    }
}
