package com.lilamaris.stockwolf.inventory.contract.event.payload;

import com.lilamaris.stockwolf.event.core.payload.EventPayload;

import java.util.UUID;

public record ReservationSucceeded(
    UUID reservationId
) implements EventPayload {
    public static ReservationSucceeded from(
            UUID reservationId
    ) {
        return new ReservationSucceeded(reservationId);
    }
}
