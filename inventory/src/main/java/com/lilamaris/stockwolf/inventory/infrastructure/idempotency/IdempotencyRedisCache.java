package com.lilamaris.stockwolf.inventory.infrastructure.idempotency;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@RequiredArgsConstructor
public class IdempotencyRedisCache implements IdempotencyCache {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }
}
