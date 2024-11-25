package com.sparta.spartascheduling.domain.user.service;


import com.sparta.spartascheduling.common.dto.AuthUser;
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


    public UserMypageDto getMypage(AuthUser authUser) {
        UserCamp userCamp = userCampRepository.findByUserId(authUser.getId());
        Camp camp = userCamp.getCamp();
        return new UserMypageDto(camp.getName());
    }
}
