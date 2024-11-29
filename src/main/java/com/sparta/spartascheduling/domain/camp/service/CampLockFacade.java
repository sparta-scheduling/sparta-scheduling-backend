package com.sparta.spartascheduling.domain.camp.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.dto.ApplyResponseDto;

@Service
public class CampLockFacade {

	private final CampService campService;
	private final RedissonClient redissonClient;

	public CampLockFacade(CampService campService, RedissonClient redissonClient) {
		this.campService = campService;
		this.redissonClient = redissonClient;
	}

	public ApplyResponseDto applyForCampRedisson(final Long campId, AuthUser authUser) {
		RLock lock = redissonClient.getLock(String.format("camp:%d", campId));
		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
			if (!available) {
				System.out.println("Redisson getLock timeout");
				throw new IllegalArgumentException();
			}
			return campService.applyForCampRedisson(campId, authUser);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
