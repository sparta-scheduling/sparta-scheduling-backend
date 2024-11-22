package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {

    // 회원 아이디 없어서 임시로
    UserCamp findByUserId(Long l);
}
