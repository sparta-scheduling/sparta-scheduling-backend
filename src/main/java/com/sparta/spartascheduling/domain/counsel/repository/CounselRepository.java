package com.sparta.spartascheduling.domain.counsel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartascheduling.domain.counsel.entity.Counsel;
import com.sparta.spartascheduling.domain.counsel.enums.CounselStatus;
import com.sparta.spartascheduling.domain.tutor.entity.Tutor;
import com.sparta.spartascheduling.domain.user.entity.User;

public interface CounselRepository extends JpaRepository<Counsel, Long> {

	Optional<Counsel> findByUserIdAndStatus(Long id, CounselStatus counselStatus);

	List<Counsel> findByTutor(Tutor tutor);

	Optional<Counsel> findByUser(User user);
}
