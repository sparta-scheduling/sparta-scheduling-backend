package com.sparta.spartascheduling.domain.tutor.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TutorMypageDto {
    private String campName;
    private List<String> students;
    public TutorMypageDto(String campName, List<String> students) {
        this.campName = campName;
        this.students = students;
    }
}
