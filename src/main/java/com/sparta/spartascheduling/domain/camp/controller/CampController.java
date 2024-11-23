package com.sparta.spartascheduling.domain.camp.controller;
import com.sparta.spartascheduling.domain.camp.service.CampService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CampController {

    private final CampService campService;

    // 캠프 신청 - 동시성 제어 할 곳
    @PostMapping("/camps/{campId}")
    public void applyForCamp(@PathVariable Long campId){
         campService.applyForCamp(campId);
    }

}
