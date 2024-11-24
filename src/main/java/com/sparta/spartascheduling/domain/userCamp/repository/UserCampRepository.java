package com.sparta.spartascheduling.domain.userCamp.repository;

import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCampRepository extends JpaRepository<UserCamp, Long> {
    int countByCampId(Long campId);
    // 튜터 아이디 없어서 임시로 - 나의캠프(튜터)
    List<UserCamp> findByCampId(Long campId);

    UserCamp findByUserId(long l);
}
