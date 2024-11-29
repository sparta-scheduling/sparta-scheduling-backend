package com.sparta.spartascheduling.domain.userCamp.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sparta.spartascheduling.common.entity.Timestamped;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "userCamps")
public class UserCamp extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "camp_id", nullable = false)
	private Camp camp;

	private UserCamp(User user, Camp camp) {
		this.user = user;
		this.camp = camp;
	}

	public static UserCamp of(User user, Camp camp) {
		return new UserCamp(user, camp);
	}

	public static class Builder {
		private User user;
		private Camp camp;

		// Setters for user and camp
		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public Builder camp(Camp camp) {
			this.camp = camp;
			return this;
		}

		public UserCamp build() {
			return new UserCamp(user, camp);
		}
	}

}
