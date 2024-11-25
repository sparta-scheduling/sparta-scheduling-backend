package com.sparta.spartascheduling.domain.counsel.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.counsel.dto.CounselRequest;
import com.sparta.spartascheduling.domain.counsel.dto.CounselResponse;
import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;
import com.sparta.spartascheduling.domain.counsel.repository.CounselRepository;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselService {

	private final CounselRepository counselRepository;
	private final UserRepository userRepository;
	private final TutorRepository tutorRepository;
	private final UserCampRepository userCampRepository;

	public CounselResponse createCounsel(AuthUser authUser, CounselRequest request) {
		// 유저 타입 확인
		if(!"USER".equals(authUser.getUserType())){
			throw new IllegalArgumentException("학생만 접근이 가능합니다.");
		}

		Long id = authUser.getId();

		// 유저 확인
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		// 튜터확인
		Tutor tutor = tutorRepository.findById(request.tutorId()).orElseThrow(()-> new IllegalArgumentException("튜터를 찾을수 없습니다."));

		UserCamp usercamp = userCampRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("유저에 맵핑된 캠프의 정보를 찾을수 없습니다."));

		// 상담 중복 확인
		Optional<Counsel> existingCounsel = counselRepository.findByUserIdAndStatus(id, CounselStatus.WAITING);
		if (existingCounsel.isPresent()) {
			throw new IllegalArgumentException("이미 진행 중인 상담이 있습니다. 새로운 상담을 신청할 수 없습니다.");
		}

		// 캠프내의 튜터에게만 상담 신청 가능
		if (!usercamp.getCamp().getId().equals(tutor.getCampId())) {
			throw new IllegalArgumentException("캠프에 있는 튜터에게만 신청이 가능합니다.");
		}

		// 현재 날짜보다 전의 날짜 상담은 신청 X
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (request.dateTime().isBefore(currentDateTime)) {
			throw new IllegalArgumentException("상담을 신청할 수 없는 날짜입니다.");
		}

		// 상담 시간 검증
		LocalTime requestTime = request.dateTime().toLocalTime();
		if (tutor.getCounselStart() != null && tutor.getCounselEnd() != null) {
			if (requestTime.isBefore(tutor.getCounselStart()) || requestTime.isAfter(tutor.getCounselEnd())) {
				throw new IllegalArgumentException("상담 시간이 아닙니다. 상담 가능 시간: "
					+ tutor.getCounselStart() + " ~ " + tutor.getCounselEnd());
			}
		}

		Counsel newCounsel = Counsel.builder()
			.user(user)
			.tutor(tutor)
			.datetime(LocalDateTime.now())
			.content(request.content())
			.status(CounselStatus.WAITING)
			.build();

		counselRepository.save(newCounsel);

		return CounselResponse.from(newCounsel);
	}

	@Transactional(readOnly = true)
	public List<CounselResponse> getCounselFromTutor(AuthUser authUser) {
		// 유저 타입 확인
		if(!"TUTOR".equals(authUser.getUserType())){
			throw new IllegalArgumentException("튜터만 접근이 가능합니다.");
		}

		Long tutorId = authUser.getId();

		// 유저 확인
		Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new IllegalArgumentException("튜터를 찾을 수 없습니다."));

		// 상담 조회
		List<Counsel> counselList = counselRepository.findByTutor(tutor);

		return counselList.stream()
			.map(CounselResponse::from)
			.toList();
	}
}
