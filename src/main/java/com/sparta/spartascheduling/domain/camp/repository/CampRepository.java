package com.sparta.spartascheduling.domain.camp.repository;

import com.sparta.spartascheduling.domain.camp.entity.Camp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampRepository extends JpaRepository<Camp, Long> {
}
