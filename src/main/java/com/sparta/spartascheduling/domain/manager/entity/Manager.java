package com.sparta.spartascheduling.domain.manager.entity;

import java.sql.Time;

import com.sparta.spartascheduling.common.entity.Timestamped;

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
}
