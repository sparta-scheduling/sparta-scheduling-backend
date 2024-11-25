package com.sparta.spartascheduling.domain.camp.repository;

import java.time.LocalDate;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import org.springframework.data.jpa.repository.Query;

public interface CampRepository extends JpaRepository<Camp, Long> {
	boolean existsByNameAndOpenDate(String name, LocalDate openDate);

    // 캠프 신청시 본인이 참여한 전체 캠프 체크
    @Query("SELECT c.status FROM Camp c WHERE c.id IN :campIds")
    List<String> findCampStatusesByIds(List<Long> campIds);

}
