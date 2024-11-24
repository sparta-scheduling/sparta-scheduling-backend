package com.sparta.spartascheduling.domain.manager.entity;

import com.sparta.spartascheduling.common.entity.Timestamped;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "managers")
public class Manager extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String username;

	// 정적 팩토리 메서드로 매니저 생성 및 유효성 검사
	public static Manager createManager(String email, String password, String username) {
		validateEmail(email);
		validatePassword(password);

		return Manager.builder()
			.email(email)
			.password(password)
			.username(username)
			.build();
	}

	// 이메일 유효성 검사 메서드
	private static void validateEmail(String email) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("이메일은 필수 입력 사항입니다.");
		}
	}

	// 비밀번호 유효성 검사 메서드
	private static void validatePassword(String password) {
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("비밀번호는 필수 입력 사항입니다.");
		}
	}

	public void setId(long l) { // 테스트용
	}
}
