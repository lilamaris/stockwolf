package com.lilamaris.stockwolf.inventory.application.service;

import com.lilamaris.stockwolf.inventory.application.exception.ApplicationErrorCode;
import com.lilamaris.stockwolf.inventory.application.exception.ApplicationResourceNotFoundException;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationEntry;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationManager;
import com.lilamaris.stockwolf.inventory.application.port.out.ReservationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements
        ReservationManager {
    private final ReservationStore reservationStore;
    private final TxService tx;

    @Override
    public ReservationEntry reserve(
            String correlationId,
            String skuId,
            int quantity
    ) {
        try {
            return tx.tryReserve(correlationId, skuId, quantity);
        } catch (DataIntegrityViolationException e) {
            var reservation = reservationStore.getByCorrelationId(correlationId)
                    .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.RESERVATION_NOT_FOUND));
            return ReservationEntry.from(reservation);
        }
    }

    @Override
    public ReservationEntry commit(String correlationId) {
        try {
            return tx.tryCommit(correlationId);
        } catch (DataIntegrityViolationException e) {
            var reservation = reservationStore.getByCorrelationId(correlationId)
                    .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.RESERVATION_NOT_FOUND));
            return ReservationEntry.from(reservation);
        }
    }

    @Override
    public ReservationEntry cancel(String correlationId) {
        try {
            return tx.tryCancel(correlationId);
        } catch (DataIntegrityViolationException e) {
            var reservation = reservationStore.getByCorrelationId(correlationId)
                    .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.RESERVATION_NOT_FOUND));
            return ReservationEntry.from(reservation);
        }
    }
}
