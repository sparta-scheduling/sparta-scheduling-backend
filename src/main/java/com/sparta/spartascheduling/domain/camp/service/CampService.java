package com.sparta.spartascheduling.domain.camp.service;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampService {
    private final CampRepository campRepository;
    private final UserCampRepository userCampRepository;
    private final UserRepository userRepository;

    public void applyForCamp(Long campId) {
        Camp camp = campRepository.findById(campId).orElseThrow(()-> new IllegalArgumentException("캠프가 존재하지 않습니다."));
        User user = userRepository.findById(1L).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 학생입니다.")); // 회원 아이디 임시

        UserCamp userCampCheck = userCampRepository.findByUserId(1L); // 회원 아이디 임시
        if(userCampCheck != null && campId == userCampCheck.getCamp().getId()){
            throw new IllegalArgumentException("이미 소속된 캠프는 신청할 수 없습니다.");
        }

        if(userCampCheck != null && userCampCheck.getCamp().getRemainCount() >= userCampCheck.getCamp().getMaxCount()){
            throw new IllegalArgumentException("정원이 초과되어서 캠프를 신청할 수 없습니다.");
        }

        UserCamp userCamp = UserCamp.of(user, camp);
        userCampRepository.save(userCamp);
    }
}
