package com.sparta.spartascheduling.domain.tutor.entity;

import java.sql.Time;
import java.time.LocalTime;

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
@NoArgsConstructor
@Table(name = "tutors")
public class Tutor extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String email;

	private String password;

	private Long campId;

	private LocalTime counselStart;

	private LocalTime counselEnd;

	@Builder
	public Tutor(Long id, String username, String email, String password, Long campId, LocalTime counselStart,
		LocalTime counselEnd) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.campId = campId;
		this.counselStart = counselStart;
		this.counselEnd = counselEnd;
	}

	public void setCounselStart(LocalTime counselStart) {
		this.counselStart = counselStart;
	}

	public void setCounselEnd(LocalTime counselEnd) {
		this.counselEnd = counselEnd;
	}

}
