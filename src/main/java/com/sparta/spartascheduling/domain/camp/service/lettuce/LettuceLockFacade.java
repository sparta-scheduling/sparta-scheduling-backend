package com.sparta.spartascheduling.domain.camp.service.lettuce;

import org.springframework.stereotype.Service;

@Service
public class LettuceLockFacade {

	private final RedisLockRepository redisLockRepository;

	public LettuceLockFacade(RedisLockRepository redisLockRepository) {
		this.redisLockRepository = redisLockRepository;
	}

	// 락을 획득하려는 캠프 id 를 받아 락을 걸기
	public Boolean acquireLock(Long campId) {
		return redisLockRepository.lock(campId);
	}

	// 락 해제
	public Boolean releaseLock(Long campId) {
		return redisLockRepository.unlock(campId);
	}

	public Boolean isLockAcquired(Long campId) {
		return redisLockRepository.isLockAcquired(campId);
	}

}
