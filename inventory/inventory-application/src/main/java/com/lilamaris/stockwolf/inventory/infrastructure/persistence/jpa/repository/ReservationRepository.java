package com.lilamaris.stockwolf.inventory.infrastructure.persistence.jpa.repository;

import com.lilamaris.stockwolf.inventory.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    boolean existsByCorrelationId(String correlationId);

    Optional<Reservation> findByCorrelationId(String correlationId);
}
