package com.sparta.spartascheduling.domain.auth.service;

import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.common.config.PasswordEncoder;
import com.sparta.spartascheduling.domain.auth.dto.request.SigninRequestDto;
import com.sparta.spartascheduling.domain.auth.dto.request.SignupRequestDto;
import com.sparta.spartascheduling.domain.auth.dto.response.SignupResponseDto;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.tutor.repository.TutorRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.enums.DeleteStatus;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.exception.customException.CustomAuthException;
import com.sparta.spartascheduling.exception.customException.ManagerException;
import com.sparta.spartascheduling.exception.customException.TutorException;
import com.sparta.spartascheduling.exception.customException.UserException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final ManagerRepository managerRepository;
	private final TutorRepository tutorRepository;
	private final PasswordEncoder passwordEncoder;

	public SignupResponseDto signup(SignupRequestDto requestDto) {

		if (userRepository.existsByEmail(requestDto.getEmail()) || managerRepository.existsByEmail(
			requestDto.getEmail()) || tutorRepository.existsByEmail(requestDto.getEmail())) {
			throw new CustomAuthException(ExceptionCode.DUPLICATED_EMAIL);
		}

		if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
			throw new CustomAuthException(ExceptionCode.NOT_MATCH_PASSWORD);
		}

		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		if (requestDto.isAdmin()) {
			Manager newManager = Manager.builder()
				.email(requestDto.getEmail())
				.password(encodedPassword)
				.username(requestDto.getUsername())
				.build();

			Manager savedManager = managerRepository.save(newManager);
			return new SignupResponseDto(savedManager.getId(), savedManager.getEmail(), savedManager.getUsername());
		}

		User newUser = User.builder()
			.email(requestDto.getEmail())
			.password(encodedPassword)
			.username(requestDto.getUsername())
			.build();

		User savedUser = userRepository.save(newUser);
		return new SignupResponseDto(savedUser.getId(), savedUser.getEmail(), savedUser.getUsername());
	}

	public void signin(SigninRequestDto requestDto) {
		if (requestDto.getUserType().equals("ADMIN")) {
			Manager existManager = managerRepository.findByEmail(requestDto.getEmail()).orElseThrow(
				() -> new ManagerException(ExceptionCode.NOT_FOUND_MANAGER)
			);

			if (!passwordEncoder.matches(requestDto.getPassword(), existManager.getPassword())) {
				throw new CustomAuthException(ExceptionCode.INVALID_PASSWORD);
			}

		} else if (requestDto.getUserType().equals("TUTOR")) {
			Tutor existTutor = tutorRepository.findByEmail(requestDto.getEmail()).orElseThrow(
				() -> new TutorException(ExceptionCode.NOT_FOUND_TUTOR)
			);

			if (!passwordEncoder.matches(requestDto.getPassword(), existTutor.getPassword())) {
				throw new CustomAuthException(ExceptionCode.INVALID_PASSWORD);
			}

		} else {
			User existUser = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
				() -> new UserException(ExceptionCode.NOT_FOUND_USER)
			);

			if (existUser.getStatus().equals(DeleteStatus.INACTIVE)) {
				throw new UserException(ExceptionCode.USER_WITHDRAWN);
			}

			if (!passwordEncoder.matches(requestDto.getPassword(), existUser.getPassword())) {
				throw new CustomAuthException(ExceptionCode.INVALID_PASSWORD);
			}
		}
	}
}
