package com.sparta.spartascheduling.domain.tutor.repository;

import java.util.Optional;

import com.sparta.spartascheduling.domain.tutor.entity.Tutor;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.tutor.entity.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
	Optional<Tutor> findByEmail(String email);
	boolean existsByEmail(String email);
}
