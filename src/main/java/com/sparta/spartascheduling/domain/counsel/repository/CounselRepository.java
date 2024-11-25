package com.sparta.spartascheduling.domain.counsel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;

public interface CounselRepository extends JpaRepository<Counsel, Long> {

	Optional<Counsel> findByUserIdAndStatus(Long id, CounselStatus counselStatus);
}
