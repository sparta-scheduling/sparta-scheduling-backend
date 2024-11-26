package com.sparta.spartascheduling.domain.camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.spartascheduling.domain.camp.entity.Camp;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CampRepository extends JpaRepository<Camp, Long> {

	// 중복 여부 확인 메서드
	boolean existsByNameAndOpenDate(String name, LocalDate openDate);

	// 상태별 우선순위를 적용하여 캠프 리스트 조회
	@Query("SELECT c FROM Camp c ORDER BY " +
		"CASE WHEN c.status = 'RECRUITING' THEN 1 " +
		"     WHEN c.status = 'CREATED' THEN 2 " +
		"     WHEN c.status = 'IN_PROGRESS' THEN 3 " +
		"     WHEN c.status = 'CLOSED' THEN 4 END, " +
		"c.openDate ASC")
	List<Camp> findAllOrderedByStatus();

	// 비관적 락을 사용하여 캠프 조회
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Camp c WHERE c.id = :campId")
	Optional<Camp> findByIdWithLock(@Param("campId") Long campId);
}
