package com.sparta.spartascheduling.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.user.dto.UserMypageDto;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.enums.DeleteStatus;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.CustomAuthException;
import com.sparta.spartascheduling.exception.customException.UserCampException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserCampRepository userCampRepository;
	private final UserRepository userRepository;

	@Transactional // lazy 로딩의 특성상 userCamp를 가져오고 나서 트랜잭션이 종료되어 LazyInitializationException 이 발생 (수정)
	public UserMypageDto getMypage(AuthUser authUser) {
		UserCamp userCamp = userCampRepository.findByUserId(authUser.getId());
		if (userCamp == null) {
			throw new UserCampException(ExceptionCode.NOT_FOUND_USER_CAMP);
		} // 유저 캠프가 없는 경우에 대한 예외처리 추가

		Camp camp = userCamp.getCamp();
		return new UserMypageDto(camp.getName());
	}

	@Transactional
	public void deleteUser(AuthUser authUser, Long userId) {
		System.out.println(authUser.getId());
		System.out.println(userId);
		System.out.println(authUser.getUserType());
		/*if ((!authUser.getUserType().equals("ADMIN")) || !(authUser.getUserType().equals("USER")
			&& authUser.getId() == userId) || (!authUser.getUserType().equals("ADMIN")))
		*/
		// 기존 조건은 ADMIN 이 아니면 true 를 반환하여 예외가 실행되도록 하는데 '||' 연산자의 경우 True 이면 그 다음
		// 조건문은 진행하지 않기 때문에 로직이 맞지 않습니다.

		if (authUser.getUserType().equals("USER") && !authUser.getId().equals(userId)) {
			throw new CustomAuthException(ExceptionCode.NO_HAVE_AUTH_DELETE);
		}
		if (authUser.getUserType().equals("TUTOR")) {
			throw new CustomAuthException(ExceptionCode.NO_HAVE_AUTH_DELETE);
		}
		// ADMIN 이 아닌 경우 그리고 user 타입도 인데 유저 id 가 불일치 하는 경우 , 튜터도 삭제 불가능

		User existUser = userRepository.findById(userId).orElseThrow(
			() -> new CustomAuthException(ExceptionCode.NOT_FOUND_USER)
		);

		if (existUser.getStatus().equals(DeleteStatus.INACTIVE)) {
			throw new CustomAuthException(ExceptionCode.USER_WITHDRAWN);
		}

		// 연관관계가 없는 상태에서는 먼저 자식의 객체를 삭제하고 부모를 삭제해야 한다.
		// 아마 캠프 삭제를 구현한다면 비슷할 것 같습니다.
		// 근데 이거 soft delete 하기로 한것 아니였나요? 일단 hard delete 로 해보긴 했습니다.
		userCampRepository.deleteUserCampsByUserId(existUser.getId());
		userRepository.delete(existUser);
	}
}
