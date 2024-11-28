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
import com.sparta.spartascheduling.exception.customException.CounselException;
import com.sparta.spartascheduling.exception.customException.TutorException;
import com.sparta.spartascheduling.exception.customException.UserCampException;
import com.sparta.spartascheduling.exception.customException.UserException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselService {

	private final CounselRepository counselRepository;
	private final UserRepository userRepository;
	private final TutorRepository tutorRepository;
	private final UserCampRepository userCampRepository;

	@Transactional
	public CounselResponse createCounsel(AuthUser authUser, CounselRequest request) {
		// 유저 타입 확인
		if (!"USER".equals(authUser.getUserType())) {
			throw new UserException(ExceptionCode.NO_AUTHORIZATION_USER);
		}

		Long id = authUser.getId();

		// 유저 확인
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionCode.NOT_FOUND_USER));

		// 튜터확인
		Tutor tutor = tutorRepository.findById(request.tutorId())
			.orElseThrow(() -> new TutorException(ExceptionCode.NOT_FOUND_TUTOR));

		UserCamp usercamp = userCampRepository.findByUserId(id);
		if (usercamp == null) {
			throw new UserCampException(ExceptionCode.NOT_FOUND_USER_CAMP);
		}
		// 상담 중복 확인
		Optional<Counsel> existingCounsel = counselRepository.findByUserIdAndStatus(id, CounselStatus.WAITING);
		if (existingCounsel.isPresent()) {
			throw new CounselException(ExceptionCode.CONSULTATION_IN_PROGRESS);
		}

		// 캠프내의 튜터에게만 상담 신청 가능
		if (!usercamp.getCamp().getId().equals(tutor.getCampId())) {
			throw new CounselException(ExceptionCode.TUTOR_NOT_IN_CAMP);
		}

		// 현재 날짜보다 전의 날짜 상담은 신청 X
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (request.dateTime().isBefore(currentDateTime)) {
			throw new CounselException(ExceptionCode.INVALID_CONSULT_DATE);
		}

		// 상담 시간 검증
		LocalTime requestTime = request.dateTime().toLocalTime();
		if (tutor.getCounselStart() != null && tutor.getCounselEnd() != null) {
			if (requestTime.isBefore(tutor.getCounselStart()) || requestTime.isAfter(tutor.getCounselEnd())) {
				throw new CounselException(ExceptionCode.INVALID_CONSULT_TIME,
					tutor.getCounselStart(), tutor.getCounselEnd());
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
		if (!"TUTOR".equals(authUser.getUserType())) {
			throw new TutorException(ExceptionCode.NO_AUTHORIZATION_TUTOR);
		}

		Long tutorId = authUser.getId();

		// 유저 확인
		Tutor tutor = tutorRepository.findById(tutorId)
			.orElseThrow(() -> new TutorException(ExceptionCode.NOT_FOUND_TUTOR));

		// 상담 조회
		List<Counsel> counselList = counselRepository.findByTutor(tutor);

		return counselList.stream()
			.map(CounselResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public CounselResponse getCounselFromUser(AuthUser authUser) {
		if (!"USER".equals(authUser.getUserType())) {
			throw new UserException(ExceptionCode.NO_AUTHORIZATION_USER);
		}

		Long userId = authUser.getId();

		// 유저 확인
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(ExceptionCode.NOT_FOUND_USER));

		// 상담 조회
		Counsel counsel = counselRepository.findByUser(user)
			.orElseThrow(() -> new CounselException(ExceptionCode.NOT_FOUND_COUNSEL));

		return CounselResponse.from(counsel);
	}
}
