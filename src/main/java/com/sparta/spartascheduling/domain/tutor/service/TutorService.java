package com.sparta.spartascheduling.domain.tutor.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;
import com.sparta.spartascheduling.domain.counsel.repository.CounselRepository;
import com.sparta.spartascheduling.domain.tutor.dto.TutorMypageDto;
import com.sparta.spartascheduling.domain.tutor.dto.TutorRequest;
import com.sparta.spartascheduling.domain.tutor.dto.TutorResponseDto;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.TutorException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TutorService {
	private final TutorRepository tutorRepository;
	private final UserCampRepository userCampRepository;
	private final CounselRepository counselRepository;

	public TutorMypageDto getTutorMypage(AuthUser authUser) {
		if (!"TUTOR".equals(authUser.getUserType())) {
			throw new TutorException(ExceptionCode.NO_AUTHORIZATION_TUTOR);
		}

		Tutor tutor = tutorRepository.findById(authUser.getId())
			.orElseThrow(() -> new TutorException(ExceptionCode.NOT_FOUND_TUTOR));
		List<UserCamp> userCamps = userCampRepository.findByCampId(tutor.getCampId());
		List<String> studentNames = userCamps.stream().map(userCamp -> userCamp.getUser().getUsername()).collect(
			Collectors.toList());
		Camp camp = userCamps.isEmpty() ? null : userCamps.get(0).getCamp();
		return new TutorMypageDto(camp.getName(), studentNames);
	}

	public TutorResponseDto updateTime(AuthUser authUser, @Valid TutorRequest request) {
		if (!"TUTOR".equals(authUser.getUserType())) {
			throw new TutorException(ExceptionCode.NO_AUTHORIZATION_TUTOR);
		}

		Tutor tutor = tutorRepository.findById(authUser.getId())
			.orElseThrow(() -> new TutorException(ExceptionCode.NOT_FOUND_TUTOR));

		List<Counsel> counselList = counselRepository.findByTutor(tutor);

		boolean hasScheduledCounsel = counselList.stream()
			.anyMatch(counsel -> counsel.getStatus() == CounselStatus.WAITING);

		if (hasScheduledCounsel) {
			throw new TutorException(ExceptionCode.ALREADY_EXIST_COUNSEL);
		}

		tutor.setCounselStart(LocalTime.from(request.counselStart()));
		tutor.setCounselEnd(LocalTime.from(request.counselEnd()));

		tutorRepository.save(tutor);

		return TutorResponseDto.from(tutor);
	}
}
