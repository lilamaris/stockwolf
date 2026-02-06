package com.lilamaris.stockwolf.idempotency.supports.cache.redis;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
@ConditionalOnClass({RedisTemplate.class, RedisConnectionFactory.class})
@AutoConfigureAfter(DataRedisAutoConfiguration.class)
public class IdempotencySupportRedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "idempotencyRedisTemplate")
    public RedisTemplate<String, Object> idempotencyRedisTemplate(
            RedisConnectionFactory factory,
            ObjectMapper mapper
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJacksonJsonRedisSerializer(mapper));
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(IdempotencyCache.class)
    IdempotencyCache idempotencyRedisCache(
            RedisTemplate<String, Object> redisTemplate
    ) {
        return new IdempotencyRedisCache(redisTemplate);
    }
}
