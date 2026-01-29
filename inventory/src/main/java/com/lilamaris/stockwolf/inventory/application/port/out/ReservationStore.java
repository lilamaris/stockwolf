package com.lilamaris.stockwolf.inventory.application.port.out;

import com.lilamaris.stockwolf.inventory.domain.Reservation;

import java.util.Optional;

public interface ReservationStore {
    Optional<Reservation> getByCorrelationId(String correlationId);

    Reservation save(Reservation reservation);
}
