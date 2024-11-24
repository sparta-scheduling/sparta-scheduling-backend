package com.sparta.spartascheduling.domain.camp.service;

import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampService {
	private final CampRepository campRepository;
	private final ManagerRepository managerRepository;

	public CampResponseDto createCamp(CampRequestDto requestDto, Long managerId) {
		Manager manager = managerRepository.findById(managerId)
			.orElseThrow(() -> new IllegalArgumentException("매니저를 찾을 수 없습니다."));

		// 중복 캠프 확인
		if (campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())) {
			throw new IllegalArgumentException("같은 이름과 시작일의 캠프가 이미 존재합니다.");
		}

		// 캠프 생성 및 저장 (엔티티의 정적 메서드 사용)
		Camp camp = Camp.createCamp(requestDto, manager);
		Camp savedCamp = campRepository.save(camp);

		return new CampResponseDto(savedCamp);
	}
}
