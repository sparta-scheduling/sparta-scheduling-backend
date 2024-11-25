package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {

    // 나의캠프(튜터)
    List<UserCamp> findByCampId(Long campId);

    // 나의 캠프(학생)
    UserCamp findByUserId(Long userId);

    // closed 제외한 상태값 1개라도 있는 경우 찾기
    @Query("SELECT COUNT(uc) > 0 FROM UserCamp uc JOIN uc.camp c WHERE uc.user.id = :userId AND c.status != :status")
    boolean existsActiveCampForUser(Long userId, CampStatus campStatus);

}
