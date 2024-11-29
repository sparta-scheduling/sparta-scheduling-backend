package com.sparta.spartascheduling.domain.camp.service.lettuce;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisLockRepository(final RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Boolean lock(final Long campId) {
		Boolean lockResult = redisTemplate
			.opsForValue()
			.setIfAbsent(generated(campId), String.format("camp:%d", campId), Duration.ofMillis(3000)); // 3초 대기

		System.out.println("결과 : " + lockResult);

		return lockResult;
	}

	public Boolean unlock(final Long campId) {
		return redisTemplate.delete(generated(campId));
	}

	// 캠프 ID에 대한 락 상태 확인
	public Boolean isLockAcquired(final Long campId) {
		return redisTemplate.hasKey(generated(campId));  // 락 키가 존재하면 락이 획득된 상태
	}

	private String generated(Long key) {
		return key.toString();
	}
}
