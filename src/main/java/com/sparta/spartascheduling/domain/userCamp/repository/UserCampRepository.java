package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {
  List<UserCamp> findByCampId(Long campId);
	UserCamp findByUserId(Long l);
}
