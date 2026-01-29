package com.lilamaris.stockwolf.inventory.application.service;

import com.lilamaris.stockwolf.inventory.application.exception.ApplicationErrorCode;
import com.lilamaris.stockwolf.inventory.application.exception.ApplicationResourceNotFoundException;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationEntry;
import com.lilamaris.stockwolf.inventory.application.port.out.InventoryStore;
import com.lilamaris.stockwolf.inventory.application.port.out.ReservationStore;
import com.lilamaris.stockwolf.inventory.domain.Inventory;
import com.lilamaris.stockwolf.inventory.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TxService {
    private final InventoryStore inventoryStore;
    private final ReservationStore reservationStore;

    @Retryable(
            includes = OptimisticLockingFailureException.class,
            maxRetries = 7,
            delay = 50,
            multiplier = 2,
            jitter = 10,
            maxDelay = 1000
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReservationEntry tryReserve(
            String correlationId,
            String skuId,
            int quantity
    ) {
        var reservation = Reservation.create(correlationId, Duration.ofMinutes(5));
        var inventory = getInventory(skuId);

        inventory.reserve(quantity);
        reservation.addItem(skuId, quantity);

        try {
            inventoryStore.save(inventory);
            reservationStore.save(reservation);
            return ReservationEntry.from(reservation);
        } catch (DataIntegrityViolationException e) {
            return ReservationEntry.from(getReservation(correlationId));
        }
    }

    private Reservation getReservation(String correlationId) {
        return reservationStore.getByCorrelationId(correlationId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.RESERVATION_NOT_FOUND));
    }

    private Inventory getInventory(String skuId) {
        return inventoryStore.get(skuId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.INVENTORY_NOT_FOUND));
    }
}
