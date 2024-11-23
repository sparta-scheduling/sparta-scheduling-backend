package com.sparta.spartascheduling.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public boolean existsByEmail(String email);
}
