package com.lilamaris.stockwolf.inventory.application.service;

import com.lilamaris.stockwolf.inventory.application.port.in.ReservationEntry;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationManager;
import com.lilamaris.stockwolf.inventory.application.port.out.ReservationStore;
import lombok.RequiredArgsConstructor;
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
        return reservationStore.getByCorrelationId(correlationId)
                .map(ReservationEntry::from)
                .orElseGet(() -> tx.tryReserve(correlationId, skuId, quantity));
    }
}
