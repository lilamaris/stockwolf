package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.store.EventStore;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelopeFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@AutoConfigureAfter(DataJpaRepositoriesAutoConfiguration.class)
@ConditionalOnClass({EntityManager.class, JpaRepository.class})
@EnableScheduling
public class EventSupportJpaAutoConfiguration {
    @Bean
    EventStore eventStore(
            JpaStoredEventEnvelopeRepository jpaStoredEventEnvelopeRepository
    ) {
        return new JpaEventStore(jpaStoredEventEnvelopeRepository);
    }

    @Bean
    StoredEventEnvelopeFactory storedEventEnvelopeFactory() {
        return new JpaStoredEventEnvelopeFactory();
    }
}
