package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.core.provider.CorrelationProvider;
import com.lilamaris.stockwolf.event.core.provider.EventIdProvider;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import com.lilamaris.stockwolf.event.core.relay.outbound.OutboundStore;
import com.lilamaris.stockwolf.event.foundation.relay.OutboundRelay;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;

@AutoConfiguration
@AutoConfigureAfter(DataJpaRepositoriesAutoConfiguration.class)
@ConditionalOnClass({EntityManager.class, JpaRepository.class})
public class EventSupportJpaAutoConfiguration {
    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    @ConditionalOnMissingBean(OutboundStore.class)
    OutboundStore outboundStore(
            OutboxEventRepository repository,
            ObjectMapper objectMapper,
            Clock clock,
            EventIdProvider eventIdProvider,
            ProducerProvider producerProvider,
            CorrelationProvider correlationProvider
    ) {
        return new OutboxStore(repository, objectMapper, clock, eventIdProvider, producerProvider, correlationProvider);
    }

    @Bean
    public JpaOutboundStoreScheduler jpaOutboundStoreScheduler(
            OutboundRelay outboundRelay
    ) {
        return new JpaOutboundStoreScheduler(outboundRelay);
    }
}
