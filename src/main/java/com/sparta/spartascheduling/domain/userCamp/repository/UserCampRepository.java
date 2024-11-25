package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {

    // 나의캠프(튜터)
    List<UserCamp> findByCampId(Long campId);

    // 나의 캠프(학생)
    UserCamp findByUserId(Long userId);

}
