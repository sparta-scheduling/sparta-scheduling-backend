package com.sparta.spartascheduling.domain.tutor.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TotorMypageDto {
    private String campName;
    private List<String> students;
    public TotorMypageDto(String campName, List<String> students) {
        this.campName = campName;
        this.students = students;
    }
}
