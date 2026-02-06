package com.lilamaris.stockwolf.idempotency.supports.store.jpa;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyStore;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

@AutoConfiguration
@AutoConfigureAfter(DataJpaRepositoriesAutoConfiguration.class)
@ConditionalOnClass({EntityManager.class, JpaRepository.class})
public class IdempotencySupportJpaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(IdempotencyStore.class)
    IdempotencyStore idempotencyJpaStore(
            IdempotencyRepository repository
    ) {
        return new IdempotencyJpaStore(repository);
    }
}
