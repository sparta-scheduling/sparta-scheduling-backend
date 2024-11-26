package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {

    // 특정 캠프에 등록된 유저-캠프 관계를 모두 조회합니다.
    List<UserCamp> findByCampId(Long campId);

    // 특정 유저가 참여 중인 유저-캠프 관계를 조회합니다.
    UserCamp findByUserId(Long userId);

    // 특정 유저가 특정 캠프에 이미 신청했는지 확인합니다.
    @Query("SELECT COUNT(uc) > 0 FROM UserCamp uc WHERE uc.user.id = :userId AND uc.camp.id = :campId")
    boolean existsByUserIdAndCampId(Long userId, Long campId);

    // 특정 유저가 참여 중인 캠프 중, 상태가 CLOSED가 아닌 캠프가 있는지 확인합니다.
    @Query("SELECT COUNT(uc) > 0 FROM UserCamp uc JOIN uc.camp c WHERE uc.user.id = :userId AND c.status != :status")
    boolean existsActiveCampForUser(Long userId, CampStatus status);
}
