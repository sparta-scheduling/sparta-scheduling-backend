package com.sparta.spartascheduling.domain.manager.repository;

import com.sparta.spartascheduling.domain.manager.entity.Manager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	boolean existsByEmail(String email);
}
