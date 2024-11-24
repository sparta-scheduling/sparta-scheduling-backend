package com.sparta.spartascheduling.domain.tutor.service;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.tutor.dto.TotorMypageDto;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;
    private final UserCampRepository userCampRepository;

    public TotorMypageDto getTutorMypage(){
        // 회원 아이디 없어서 임시로
        Tutor tutor = tutorRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 튜터입니다."));
        List<UserCamp> userCamps = userCampRepository.findByCampId(tutor.getCampId());

        // user entity에 이름이 없어서 임시로 이메일 가져오는걸로 대체(엔티티 공통 부분이라 추후 소통 후 수정되면 이름으로 교체)
        List<String> userEmails = userCamps.stream().map(userCamp -> userCamp.getUser().getEmail()).collect(
            Collectors.toList());
        Camp camp = userCamps.isEmpty() ? null : userCamps.get(0).getCamp();
        return new TotorMypageDto(camp.getName(), userEmails);
    }
}
