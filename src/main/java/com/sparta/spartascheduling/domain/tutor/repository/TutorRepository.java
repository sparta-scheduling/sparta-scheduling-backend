package com.sparta.spartascheduling.domain.tutor.repository;

import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}
