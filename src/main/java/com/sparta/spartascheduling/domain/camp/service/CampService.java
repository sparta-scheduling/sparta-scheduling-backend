package com.sparta.spartascheduling.domain.camp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.customException.ManagerException;
import com.sparta.spartascheduling.exception.customException.UserException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampService {

	private final CampRepository campRepository;
	private final ManagerRepository managerRepository;
	private final UserRepository userRepository;
	private final UserCampRepository userCampRepository;

	// 캠프 생성 - [홍주영 파트]
	@Transactional
	public CampResponseDto createCamp(CampRequestDto requestDto, AuthUser authUser) {
		// ADMIN 권한 검증
		if (!"ADMIN".equals(authUser.getUserType())) {
			throw new CampException(ExceptionCode.NO_AUTHORIZATION_ADMIN);
		}

		// 매니저 조회
		Manager manager = managerRepository.findByEmail(authUser.getEmail())
			.orElseThrow(() -> new ManagerException(ExceptionCode.NOT_FOUND_MANAGER));

		// 중복 캠프 확인
		if (campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())) {
			throw new CampException(ExceptionCode.ALREADY_EXIST_CAMP);
		}

		// 캠프 생성 및 저장
		Camp camp = Camp.createCamp(
			requestDto.getName(),
			requestDto.getContents(),
			requestDto.getOpenDate(),
			requestDto.getCloseDate(),
			requestDto.getMaxCount(),
			manager
		);
		Camp savedCamp = campRepository.save(camp);
		return CampResponseDto.from(savedCamp);
	}

	// 캠프 단건 조회
	@Transactional(readOnly = true)
	public CampResponseDto getCampById(Long campId) {
		// 캠프 조회
		Camp camp = campRepository.findById(campId)
			.orElseThrow(() -> new CampException(ExceptionCode.NOT_FOUND_CAMP));

		// 조회된 camp 엔티티를 기반으로 DTO 생성 및 반환
		return CampResponseDto.from(camp);
	}

	// 캠프 리스트 조회
	@Transactional(readOnly = true)
	public List<CampResponseDto> getAllCamps() {
		// 상태별 우선순위를 적용하여 캠프 리스트를 반환
		List<Camp> camps = campRepository.findAllOrderedByStatus();

		return camps.stream()
			.map(CampResponseDto::from)
			.collect(Collectors.toList());
	}

	// 캠프 신청 - [문정원 파트]
	@Transactional
	public UserCamp applyForCamp(Long campId, AuthUser authUser) {
		// USER 권한 확인
		if (!"USER".equals(authUser.getUserType())) {
			throw new UserException(ExceptionCode.NO_AUTHORIZATION_USER);
		}

		// 비관적 락을 사용하여 캠프 조회
		Camp camp = campRepository.findByIdWithLock(campId)
			.orElseThrow(() -> new CampException(ExceptionCode.NOT_FOUND_CAMP));

		// 남은 인원 확인
		if (camp.getRemainCount() <= 0) {
			throw new CampException(ExceptionCode.EXCEEDED_CAMP_CAPACITY);
		}

		// 중복 신청 확인 추가
		boolean alreadyApplied = userCampRepository.existsByUserIdAndCampId(authUser.getId(), campId);
		if (alreadyApplied) {
			throw new CampException(ExceptionCode.ALREADY_APPLY_CAMP);
		}

		// 유저 조회
		User user = userRepository.findById(authUser.getId())
			.orElseThrow(() -> new UserException(ExceptionCode.NOT_FOUND_USER));

		// 남은 인원 감소
		camp.decreaseRemainCount();
		campRepository.save(camp);

		// 유저-캠프 관계 등록
		UserCamp userCamp = UserCamp.of(user, camp);
		return userCampRepository.save(userCamp);
	}
}
