package com.lilamaris.stockwolf.idempotency.supports.cache.redis;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public class IdempotencyRedisCache implements IdempotencyCache {
    private final RedisTemplate<String, Object> redisTemplate;

    public IdempotencyRedisCache(
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }
}
