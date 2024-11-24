package com.sparta.spartascheduling.domain.user.dto;

import lombok.Getter;

@Getter
public class UserMypageDto {

    private String campName;
    public UserMypageDto(String campName) {
        this.campName = campName;
    }
}
