package com.lilamaris.stockwolf.idempotency;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyExecutor;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCacheKeyBuilder;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyStore;
import com.lilamaris.stockwolf.idempotency.foundation.DefaultIdempotencyExecutor;
import com.lilamaris.stockwolf.idempotency.foundation.store.NoOpIdempotencyCache;
import com.lilamaris.stockwolf.idempotency.foundation.store.PrefixedIdempotencyCacheKeyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.util.Optional;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "spring.idempotency",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(IdempotencyProperties.class)
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class IdempotencyAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(IdempotencyAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }

    @Bean
    @ConditionalOnMissingBean(IdempotencyCache.class)
    IdempotencyCache noOpCache() {
        return new NoOpIdempotencyCache();
    }

    @Bean
    @ConditionalOnMissingBean(IdempotencyCacheKeyBuilder.class)
    IdempotencyCacheKeyBuilder idempotencyCacheKeyBuilder(
            IdempotencyProperties properties
    ) {
        return new PrefixedIdempotencyCacheKeyBuilder(properties.cache().keyPrefix());
    }

    @Bean
    @ConditionalOnMissingBean(IdempotencyExecutor.class)
    IdempotencyExecutor idempotencyExecutor(
            IdempotencyStore store,
            IdempotencyCache idempotencyCache,
            IdempotencyCacheKeyBuilder idempotencyCacheKeyBuilder,
            ObjectMapper mapper,
            IdempotencyProperties properties
    ) {
        if (idempotencyCache instanceof NoOpIdempotencyCache) {
            log.warn("!!! NO AVAILABLE CACHE STORE !!!");
        }
        Duration cacheTtl = Optional.ofNullable(properties.cache().ttl())
                .orElseGet(() -> Duration.ofHours(24));

        Duration pendingTimeout = Optional.ofNullable(properties.executionTtl())
                .orElseGet(() -> Duration.ofSeconds(5));

        return new DefaultIdempotencyExecutor(
                store,
                idempotencyCache,
                idempotencyCacheKeyBuilder,
                cacheTtl,
                pendingTimeout,
                mapper
        );
    }
}
