package com.sparta.spartascheduling.domain.user.entity;

import java.time.LocalDateTime;

import com.sparta.spartascheduling.common.entity.Timestamped;
import com.sparta.spartascheduling.domain.user.enums.DeleteStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	private String password;

	private String username;

	private LocalDateTime deletedAt;

	private DeleteStatus status;

	@Builder
	private User(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.deletedAt = null;
		this.status = DeleteStatus.ACTIVE;
	}
}
