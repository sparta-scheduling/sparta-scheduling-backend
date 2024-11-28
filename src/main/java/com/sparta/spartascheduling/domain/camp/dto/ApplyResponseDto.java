package com.sparta.spartascheduling.domain.camp.dto;

import com.sparta.spartascheduling.domain.camp.entity.Camp;

import lombok.Getter;

@Getter
public class ApplyResponseDto {
	private int remainCount;

	public ApplyResponseDto(Camp camp) {
		this.remainCount = camp.getRemainCount();
	}
}
