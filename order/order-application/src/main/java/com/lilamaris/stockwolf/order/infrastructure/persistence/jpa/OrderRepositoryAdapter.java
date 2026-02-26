package com.lilamaris.stockwolf.order.infrastructure.persistence.jpa;

import com.lilamaris.stockwolf.order.application.port.out.OrderStore;
import com.lilamaris.stockwolf.order.domain.Order;
import com.lilamaris.stockwolf.order.infrastructure.persistence.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderStore {
    private final OrderRepository repository;

    @Override
    public Optional<Order> getByCorrelationId(String correlationId) {
        return repository.findByCorrelationId(correlationId);
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }
}
