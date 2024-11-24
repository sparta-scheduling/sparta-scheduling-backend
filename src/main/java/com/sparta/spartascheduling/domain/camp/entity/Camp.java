package com.sparta.spartascheduling.domain.camp.entity;

import java.util.Date;

import com.sparta.spartascheduling.common.entity.Timestamped;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.manager.entity.Manager;

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
@Table(name = "camps")
public class Camp extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	private String name;

	private String contents;

	private Date openDate;

	private Date closeDate;

	private CampStatus status;

	private int maxCount;

	private int remainCount;


	// 캠프신청될때 남은인원 -1
	public void decreaseRemainCount() {
		if(remainCount <= 0){
			throw new IllegalArgumentException("이미 남은인원이 0입니다.");
		}
		this.remainCount--;
	}


}
