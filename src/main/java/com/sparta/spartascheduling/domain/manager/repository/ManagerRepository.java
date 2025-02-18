package com.sparta.spartascheduling.domain.manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.manager.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	boolean existsByEmail(String email);
	Optional<Manager> findByEmail(String email);
}
