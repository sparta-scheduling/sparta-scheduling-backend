package com.sparta.spartascheduling.domain.user.service;


import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.user.dto.UserMypageDto;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.enums.DeleteStatus;
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


    public UserMypageDto getMypage(AuthUser authUser) {
        UserCamp userCamp = userCampRepository.findByUserId(authUser.getId());
        Camp camp = userCamp.getCamp();
        return new UserMypageDto(camp.getName());
    }

    public void deleteUser(AuthUser authUser, Long userId) {
        if ((!authUser.getUserType().equals("ADMIN")) || !(authUser.getUserType().equals("USER") && authUser.getId() == userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        User existUser = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        if (existUser.getStatus().equals(DeleteStatus.INACTIVE)) {
            throw new IllegalArgumentException("이미 삭제된 유저입니다.");
        }

        userRepository.delete(existUser);
    }
}
