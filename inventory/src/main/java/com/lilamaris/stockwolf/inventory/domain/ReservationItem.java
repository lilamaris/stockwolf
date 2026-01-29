package com.lilamaris.stockwolf.inventory.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "reservation_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, updatable = false)
    private Reservation reservation;

    private String skuId;

    private int quantity;

    protected static ReservationItem create(Reservation reservation, String skuId, int quantity) {
        var reservationItem = new ReservationItem();
        reservationItem.reservation = reservation;
        reservationItem.skuId = skuId;
        reservationItem.quantity = quantity;
        return reservationItem;
    }
}
