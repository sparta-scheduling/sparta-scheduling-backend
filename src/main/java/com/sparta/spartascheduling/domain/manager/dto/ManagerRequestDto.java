package com.sparta.spartascheduling.domain.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManagerRequestDto {
	private String email;
	private String password;
	private String username;
}
