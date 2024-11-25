package com.sparta.spartascheduling.domain.camp.service;
import com.sparta.spartascheduling.common.dto.AuthUser;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CampService {

	private final CampRepository campRepository;
	private final UserCampRepository userCampRepository;
	private final UserRepository userRepository;
	private final ManagerRepository managerRepository;

	public CampResponseDto createCamp(CampRequestDto requestDto, AuthUser authUser) {
		// userType 검증
		if (!"ADMIN".equals(authUser.getUserType())) {
			throw new IllegalArgumentException("ADMIN 권한이 필요합니다.");
		}
		// 매니저 조회
		Manager manager = managerRepository.findByEmail(authUser.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("매니저를 찾을 수 없습니다."));
		// 중복 캠프 확인
		if (campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())) {
			throw new IllegalArgumentException("같은 이름과 시작일의 캠프가 이미 존재합니다.");
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

	@Transactional()
	public void applyForCamp(Long campId, AuthUser authUser) {
		if (!"USER".equals(authUser.getUserType())) {
			throw new IllegalArgumentException("학생만 신청할 수 있습니다.");
		}

		Camp camp = campRepository.findById(campId).orElseThrow(()-> new IllegalArgumentException("캠프가 존재하지 않습니다."));
		User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 학생입니다."));
		UserCamp userCampCheck = userCampRepository.findByUserId(authUser.getId());

		// 회원정보 id 기준으로 캠프 ID 리스트 조회.
		List<Long> campIds = userCampRepository.findCampIdsByUserId(authUser.getId());

		// 여러 캠프 상태를 한번에 조회
		List<String> campStatuses = campRepository.findCampStatusesByIds(campIds);

		// 캠프 상태 중 하나라도 "COMPLETED"가 아닌 경우 예외 발생
		if (!campStatuses.contains("COMPLETED")) {
			throw new IllegalArgumentException("현재 다른 캠프를 이미 참여중입니다.");
		}

		if(userCampCheck != null && campId == userCampCheck.getCamp().getId()){
			throw new IllegalArgumentException("중복된 캠프입니다.(신청한 이력이 존재하는 캠프입니다.)");
		}

		if (userCampCheck != null && userCampCheck.getCamp().getRemainCount() <= 0){
			throw new IllegalArgumentException("정원이 초과되어서 캠프를 신청할 수 없습니다.");
		}

		// camp 엔티티에서 캠프신청될때 남은인원 -1 메서드 실행
		camp.decreaseRemainCount();
		campRepository.save(camp); // camp 테이블에 업데이트 진행

		// 캠프신청 등록
		UserCamp userCamp = UserCamp.of(user, camp);
		userCampRepository.save(userCamp);
	}
}
