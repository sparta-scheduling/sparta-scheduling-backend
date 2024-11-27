package com.sparta.spartascheduling.common.util;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLockUtil {

	private final StringRedisTemplate redisTemplate;

	public boolean lock(String key, String value, Duration timeout) {
		Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
		return Boolean.TRUE.equals(success);
	}

	public void unlock(String key) {
		redisTemplate.delete(key);
	}
}
