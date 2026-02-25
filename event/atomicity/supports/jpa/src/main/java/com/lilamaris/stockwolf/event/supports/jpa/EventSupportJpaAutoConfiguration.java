package com.lilamaris.stockwolf.event.supports.jpa;

import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

@AutoConfiguration
@AutoConfigureAfter(DataJpaRepositoriesAutoConfiguration.class)
@ConditionalOnClass({EntityManager.class, JpaRepository.class})
public class EventSupportJpaAutoConfiguration {
}
