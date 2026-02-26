package com.lilamaris.stockwolf.identity.infrastructure.persistence.redis;

import com.lilamaris.stockwolf.identity.application.exception.IdentityErrorCode;
import com.lilamaris.stockwolf.identity.application.exception.IdentityIllegalStateException;
import com.lilamaris.stockwolf.identity.application.port.out.RefreshTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisAdapter implements RefreshTokenStore {
    private final RedisTemplate<String, String> template;

    @Override
    public String consume(String key) {
        var value = template.opsForValue().getAndDelete(keyPrefix(key));
        if (value == null) {
            throw new IdentityIllegalStateException(IdentityErrorCode.TOKEN_INVALID);
        }
        return value;
    }

    @Override
    public void save(String key, String value, Duration ttl) {
        template.opsForValue().set(keyPrefix(key), value, ttl);
    }

    private String keyPrefix(String key) {
        return "identity:refresh-token:" + key;
    }
}
