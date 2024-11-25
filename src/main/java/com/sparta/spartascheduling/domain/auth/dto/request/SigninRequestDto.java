package com.sparta.spartascheduling.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequestDto {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@Pattern(regexp = "USER|TUTOR|ADMIN", message = "Role must be USER, TUTOR, or ADMIN")
	private String userType;
}
