package com.sparta.spartascheduling.domain.tutor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.tutor.entity.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}
