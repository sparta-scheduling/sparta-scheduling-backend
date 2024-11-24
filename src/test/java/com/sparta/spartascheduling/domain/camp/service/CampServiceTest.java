package com.sparta.spartascheduling.domain.camp.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CampServiceTest {

    @Autowired
    private CampService campService;

    @Test
    @DisplayName("캠프 신청")
    void test1(){

        Long campId = 3L;
        Long userId = 1L;

        // 현재 userId 강제로 넣어뒀는데 커스텀어노테이션으로 회원정보 가져올때 매개변수 받아서 진행으로 변경예정
        // 현재는 무조건 실패
        campService.applyForCamp(campId);
    }

}
