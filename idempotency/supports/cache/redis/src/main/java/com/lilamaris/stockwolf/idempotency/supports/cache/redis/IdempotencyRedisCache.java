package com.lilamaris.stockwolf.idempotency.supports.cache.redis;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;
import org.springframework.data.redis.core.RedisTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

public class IdempotencyRedisCache implements IdempotencyCache {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public IdempotencyRedisCache(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper mapper
    ) {
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    @Override
    public <T> T get(String key, Class<T> expect) {
        var entry = redisTemplate.opsForValue().get(key);
        return mapper.convertValue(entry, expect);
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }
}
