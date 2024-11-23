package com.sparta.spartascheduling.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponseDto {
	private Long id;

	private String username;

	private String email;

	public SignupResponseDto(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}

}
