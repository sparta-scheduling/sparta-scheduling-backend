package com.sparta.spartascheduling.domain.camp.service;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CampApplyTest {

    @Autowired
    private CampService campService;

    @Autowired
    private CampRepository campRepository;  // CampService에 의존하는 Repository 모킹

    @Autowired
    private UserRepository userRepository;  // UserService에 의존하는 Repository 모킹

    @Test
    @DisplayName("캠프 신청")
    void test1() {
        Long campId = 3L;
        Long userId = 1L;

        // CampRepository와 UserRepository의 동작 정의
        Mockito.when(campRepository.findById(campId));
        Mockito.when(userRepository.findById(userId));

        // 캠프 신청 메소드 호출
        //campService.applyForCamp(campId, userId);

        Mockito.verify(campRepository, Mockito.times(1)).findById(campId);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    }
}
