package com.sparta.spartascheduling.domain.camp.service;

import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampService {
	private final CampRepository campRepository;
	private final ManagerRepository managerRepository;
	private final UserCampRepository userCampRepository;

	// 캠프 생성 메서드
	public CampResponseDto createCamp(CampRequestDto requestDto, Long managerId) {
		Manager manager = managerRepository.findById(managerId)
			.orElseThrow(() -> new IllegalArgumentException("매니저를 찾을 수 없습니다."));

		// 중복 캠프 확인을 레포지토리로 위임
		campRepository.checkDuplicateCamp(requestDto.getName(), requestDto.getOpenDate());

		// 캠프 생성 및 저장
		Camp camp = Camp.createCamp(requestDto, manager);
		campRepository.save(camp);

		return CampResponseDto.from(camp);
	}

	// 캠프 단건 조회 메서드
	public CampResponseDto getCampById(Long campId) {
		Camp camp = campRepository.findById(campId)
			.orElseThrow(() -> new IllegalArgumentException("해당 캠프를 찾을 수 없습니다."));
		// 캠프 상태 업데이트 호출
		camp.updateStatus();
		// 참여자 수 조회
		int participantCount = userCampRepository.countByCampId(campId);
		// 남은 인원수 계산 로직을 엔티티로 이동
		int remainCount = camp.calculateRemainCount(participantCount);

		return CampResponseDto.from(camp, remainCount);
	}
}
