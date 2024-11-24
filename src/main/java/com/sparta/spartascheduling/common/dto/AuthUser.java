package com.sparta.spartascheduling.common.dto;

import lombok.Getter;

@Getter
public class AuthUser {

	private final Long id;
	private final String email;
	private final String username;
	private final String userType;

	public AuthUser(Long id, String email, String username, String userType) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.userType = userType;
	}
}
