package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {

    // 나의캠프(튜터)
    List<UserCamp> findByCampId(Long campId);

    // 나의 캠프(학생)
    UserCamp findByUserId(Long userId);

    // 현재 다른 캠프를 이미 참여중 예외
    @Query("SELECT uc.camp.id FROM UserCamp uc WHERE uc.user.id = :userId")
    List<Long> findCampIdsByUserId(Long userId);

}
