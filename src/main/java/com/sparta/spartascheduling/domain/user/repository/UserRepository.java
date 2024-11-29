package com.sparta.spartascheduling.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
}
