package com.lilamaris.stockwolf.inventory.infrastructure.persistence.jpa;

import com.lilamaris.stockwolf.inventory.application.port.out.ReservationStore;
import com.lilamaris.stockwolf.inventory.domain.Reservation;
import com.lilamaris.stockwolf.inventory.infrastructure.persistence.jpa.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationStore {
    private final ReservationRepository repository;
    private final EntityManager em;

    @Override
    public Optional<Reservation> getByCorrelationId(String correlationId) {
        return repository.findByCorrelationId(correlationId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }
}
