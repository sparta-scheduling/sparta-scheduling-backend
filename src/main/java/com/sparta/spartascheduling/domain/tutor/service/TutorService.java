package com.sparta.spartascheduling.domain.tutor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.tutor.dto.TutorMypageDto;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.TutorException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TutorService {
	private final TutorRepository tutorRepository;
	private final UserCampRepository userCampRepository;

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
}
