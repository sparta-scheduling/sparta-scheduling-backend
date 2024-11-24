package com.sparta.spartascheduling.domain.user.service;


import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.user.dto.UserMypageDto;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserCampRepository userCampRepository;
    private final UserRepository userRepository;

    public UserMypageDto getMypage() {
        // 회원 아이디 없어서 임시로
        UserCamp userCamp = userCampRepository.findByUserId(1L);
        Camp camp = userCamp.getCamp();
        return new UserMypageDto(camp.getName());
    }
}
