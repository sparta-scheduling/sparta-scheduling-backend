package com.sparta.spartascheduling.domain.auth.service;

import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.common.config.PasswordEncoder;
import com.sparta.spartascheduling.common.config.JwtUtil;
import com.sparta.spartascheduling.domain.auth.dto.request.SignupRequestDto;
import com.sparta.spartascheduling.domain.auth.dto.response.SignupResponseDto;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final ManagerRepository managerRepository;
	private final PasswordEncoder passwordEncoder;
	//private final JwtUtil jwtUtil;

	public SignupResponseDto signup(SignupRequestDto requestDto) {

		if (userRepository.existsByEmail(requestDto.getEmail()) || managerRepository.existsByEmail(requestDto.getEmail())) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
			throw new IllegalArgumentException("비밀번호를 동일하게 입력해 주십시오.");
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
}
