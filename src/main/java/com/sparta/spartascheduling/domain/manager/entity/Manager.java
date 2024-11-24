package com.sparta.spartascheduling.domain.manager.entity;

import com.sparta.spartascheduling.common.entity.Timestamped;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자
@Table(name = "managers")
public class Manager extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String email;

	private String password;

	@Builder
	private Manager(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	// 테스트용 임시 생성
	public static Manager createManager(String email, String password, String username) {
		return Manager.builder()
			.username(username)
			.email(email)
			.password(password)
			.build();
	}

	public void setId(long l) { // 테스트용 임시 생성
	}
}
