package com.sparta.spartascheduling.domain.tutor.service;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.tutor.dto.TutorMypageDto;
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

    public TutorMypageDto getTutorMypage(AuthUser authUser){
        if(!"TUTOR".equals(authUser.getUserType())){
            throw new IllegalArgumentException("튜터만 접근이 가능합니다.");
        }

        Tutor tutor = tutorRepository.findById(authUser.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 튜터입니다."));
        List<UserCamp> userCamps = userCampRepository.findByCampId(tutor.getCampId());
        List<String> studentNames = userCamps.stream().map(userCamp -> userCamp.getUser().getUsername()).collect(
            Collectors.toList());
        Camp camp = userCamps.isEmpty() ? null : userCamps.get(0).getCamp();
        return new TutorMypageDto(camp.getName(), studentNames);
    }
}
