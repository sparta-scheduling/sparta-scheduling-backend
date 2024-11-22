package com.sparta.spartascheduling.domain.user.repository;

import com.sparta.spartascheduling.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
