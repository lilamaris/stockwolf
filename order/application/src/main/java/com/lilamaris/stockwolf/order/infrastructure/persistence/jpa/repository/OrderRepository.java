package com.lilamaris.stockwolf.order.infrastructure.persistence.jpa.repository;

import com.lilamaris.stockwolf.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByCorrelationId(String correlationId);
}
