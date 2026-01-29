package com.lilamaris.stockwolf.inventory.application.port.in;

import com.lilamaris.stockwolf.inventory.domain.Reservation;
import com.lilamaris.stockwolf.inventory.domain.ReservationStatus;

import java.time.Instant;

public record ReservationEntry(
        String correlationId,
        ReservationStatus status,
        Instant expiresAt
) {
    public static ReservationEntry from(Reservation reservation) {
        return new ReservationEntry(
                reservation.getCorrelationId(),
                reservation.getStatus(),
                reservation.getExpiresAt()
        );
    }
}
