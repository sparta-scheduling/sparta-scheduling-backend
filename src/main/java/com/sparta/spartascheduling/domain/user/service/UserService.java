package com.sparta.spartascheduling.domain.user.service;

import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.user.dto.UserMypageDto;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.enums.DeleteStatus;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.CustomAuthException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

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
		if ((!authUser.getUserType().equals("ADMIN")) || !(authUser.getUserType().equals("USER")
			&& authUser.getId() == userId)) {
			throw new CustomAuthException(ExceptionCode.NO_AUTHORIZATION_USER);
		}

		User existUser = userRepository.findById(userId).orElseThrow(
			() -> new CustomAuthException(ExceptionCode.NOT_FOUND_USER)
		);

		if (existUser.getStatus().equals(DeleteStatus.INACTIVE)) {
			throw new CustomAuthException(ExceptionCode.USER_WITHDRAWN);
		}

		userRepository.delete(existUser);
	}
}
