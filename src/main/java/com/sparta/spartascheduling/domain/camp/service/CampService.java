package com.sparta.spartascheduling.domain.camp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampService {

	private static final Logger log = LoggerFactory.getLogger(CampService.class);

	private final CampRepository campRepository;
	private final ManagerRepository managerRepository;
	private final UserRepository userRepository;
	private final UserCampRepository userCampRepository;

	@PersistenceContext
	private EntityManager entityManager; // EntityManager 주입받아 사용

	// 캠프 생성 로직
	@Transactional
	public CampResponseDto createCamp(CampRequestDto requestDto, AuthUser authUser) {
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

		return CampResponseDto.from(campRepository.save(camp));
	}

	// 캠프 단건 조회
	@Transactional(readOnly = true)
	public CampResponseDto getCampById(Long campId) {
		Camp camp = campRepository.findById(campId)
			.orElseThrow(() -> new CampException(ExceptionCode.NOT_FOUND_CAMP));
		return CampResponseDto.from(camp);
	}

	// 캠프 리스트 조회
	@Transactional(readOnly = true)
	public List<CampResponseDto> getAllCamps() {
		return campRepository.findAllOrderedByStatus()
			.stream()
			.map(CampResponseDto::from)
			.collect(Collectors.toList());
	}

	// 캠프 신청 로직
	@Transactional
	public UserCamp applyForCamp(Long campId, AuthUser authUser) {
		if (!"USER".equals(authUser.getUserType())) {
			throw new UserException(ExceptionCode.NO_AUTHORIZATION_USER);
		}

		try {
			// 비관적 락(PESSIMISTIC_WRITE)을 사용해 캠프 정보를 가져옴. 타임아웃을 5초로 설정
			Camp camp = entityManager.find(
				Camp.class,
				campId,
				LockModeType.PESSIMISTIC_WRITE,
				Map.of("javax.persistence.lock.timeout", 5000) // 타임아웃 5초 설정
			);

			// 캠프의 남은 인원이 0 이하인 경우 예외를 발생
			if (camp.getRemainCount() <= 0) {
				throw new CampException(ExceptionCode.EXCEEDED_CAMP_CAPACITY);
			}

			// 유저 확인
			User user = userRepository.findById(authUser.getId())
				.orElseThrow(() -> new UserException(ExceptionCode.NOT_FOUND_USER));

			// 유저가 이미 신청했는지 확인
			UserCamp userCampCheck = userCampRepository.findByUserId(authUser.getId());
			if (userCampCheck != null && campId.equals(userCampCheck.getCamp().getId())) {
				throw new CampException(ExceptionCode.ALREADY_APPLY_CAMP);
			}

			// 남은 인원을 감소시키고 엔티티를 저장
			camp.decreaseRemainCount();
			entityManager.persist(camp); // 비관적 락을 사용한 상태에서 remainCount 업데이트

			// 유저-캠프 관계 생성 및 저장
			return userCampRepository.save(UserCamp.of(user, camp));

		} catch (LockTimeoutException e) {
			// 타임아웃 발생 시 로그를 기록하고 사용자 정의 예외를 발생
			log.error("Lock timeout while applying for camp with ID: {}", campId, e);
			throw new CampException(ExceptionCode.CAMP_LOCK_TIMEOUT);
		}
	}
}
