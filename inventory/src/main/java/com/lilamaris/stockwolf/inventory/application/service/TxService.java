package com.lilamaris.stockwolf.inventory.application.service;

import com.lilamaris.stockwolf.inventory.application.exception.ApplicationErrorCode;
import com.lilamaris.stockwolf.inventory.application.exception.ApplicationResourceNotFoundException;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationEntry;
import com.lilamaris.stockwolf.inventory.application.port.out.InventoryStore;
import com.lilamaris.stockwolf.inventory.application.port.out.ReservationStore;
import com.lilamaris.stockwolf.inventory.domain.Inventory;
import com.lilamaris.stockwolf.inventory.domain.Reservation;
import com.lilamaris.stockwolf.inventory.domain.ReservationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TxService {
    private final InventoryStore inventoryStore;
    private final ReservationStore reservationStore;

    @Transactional
    public ReservationEntry tryReserve(
            String correlationId,
            String skuId,
            int quantity
    ) {
        var reservation = Reservation.create(correlationId, Duration.ofMinutes(5));
        var inventory = inventoryStore.get(skuId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.INVENTORY_NOT_FOUND));

        inventory.reserve(quantity);
        reservation.addItem(skuId, quantity);

        inventoryStore.save(inventory);
        reservationStore.save(reservation);
        return ReservationEntry.from(reservation);
    }

    @Transactional
    public ReservationEntry tryCommit(String correlationId) {
        var reservation = reservationStore.getByCorrelationId(correlationId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.RESERVATION_NOT_FOUND));
        if (!reservation.isCommittable()) {
            return ReservationEntry.from(reservation);
        }

        var skuIds = reservation.getItems().stream().map(ReservationItem::getSkuId).toList();
        var inventories = inventoryStore.getAll(skuIds).stream().collect(Collectors.toUnmodifiableMap(
                Inventory::getSkuId,
                Function.identity()
        ));

        reservation.commit();
        reservation.getItems().forEach(item -> {
            var inventory = Optional.ofNullable(inventories.get(item.getSkuId()))
                    .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.INVENTORY_NOT_FOUND));
            inventory.commit(item.getQuantity());
        });

        inventoryStore.saveAll(inventories.values().stream().toList());
        reservationStore.save(reservation);

        return ReservationEntry.from(reservation);
    }

    @Transactional
    public ReservationEntry tryCancel(String correlationId) {
        var reservation = reservationStore.getByCorrelationId(correlationId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.RESERVATION_NOT_FOUND));
        if (!reservation.isCancelable()) {
            return ReservationEntry.from(reservation);
        }

        var skuIds = reservation.getItems().stream().map(ReservationItem::getSkuId).toList();
        var inventories = inventoryStore.getAll(skuIds).stream().collect(Collectors.toUnmodifiableMap(
                Inventory::getSkuId,
                Function.identity()
        ));

        reservation.cancel();
        reservation.getItems().forEach(item -> {
            var inventory = Optional.ofNullable(inventories.get(item.getSkuId()))
                    .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.INVENTORY_NOT_FOUND));
            inventory.release(item.getQuantity());
        });

        inventoryStore.saveAll(inventories.values().stream().toList());
        reservationStore.save(reservation);

        return ReservationEntry.from(reservation);
    }
}
