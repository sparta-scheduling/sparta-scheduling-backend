package com.sparta.spartascheduling.domain.camp.dto;

import com.sparta.spartascheduling.domain.camp.entity.Camp;

public class ApplyResponseDto {
	private int remainCount;

	public ApplyResponseDto(Camp camp) {
		this.remainCount = camp.getRemainCount();
	}
}
