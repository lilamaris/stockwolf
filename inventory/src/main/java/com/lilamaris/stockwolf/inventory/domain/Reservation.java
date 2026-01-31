package com.lilamaris.stockwolf.inventory.domain;

import com.lilamaris.stockwolf.inventory.domain.exception.DomainErrorCode;
import com.lilamaris.stockwolf.kernel.foundation.DomainIllegalArgumentException;
import com.lilamaris.stockwolf.kernel.foundation.DomainIllegalStateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "reservation",
        uniqueConstraints = @UniqueConstraint(columnNames = "correlation_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "correlation_id", nullable = false, updatable = false)
    private String correlationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationItem> items = new ArrayList<>();

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Version
    private Long version;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Reservation create(String correlationId, Duration ttl) {
        var reservation = new Reservation();
        reservation.correlationId = correlationId;
        reservation.status = ReservationStatus.RESERVED;
        reservation.expiresAt = Instant.now().plus(ttl);
        return reservation;
    }

    public void addItem(String skuId, int quantity) {
        if (status != ReservationStatus.RESERVED) {
            throw new DomainIllegalStateException(DomainErrorCode.INVALID_RESERVATION_STATUS);
        }
        if (quantity <= 0) {
            throw new DomainIllegalArgumentException(DomainErrorCode.INVALID_QUANTITY);
        }
        var item = ReservationItem.create(this, skuId, quantity);
        this.items.add(item);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isReserved() {
        return status == ReservationStatus.RESERVED;
    }

    public boolean isCommitted() {
        return status == ReservationStatus.COMMITTED;
    }

    public boolean isCommittable() {
        return status == ReservationStatus.RESERVED;
    }

    public boolean isCanceled() {
        return status == ReservationStatus.CANCELED;
    }

    public boolean isCancelable() {
        return status == ReservationStatus.RESERVED;
    }

    public void commit() {
        if (status != ReservationStatus.RESERVED) {
            throw new DomainIllegalStateException(DomainErrorCode.INVALID_RESERVATION_STATUS);
        }
        this.status = ReservationStatus.COMMITTED;
    }

    public void cancel() {
        if (status == ReservationStatus.COMMITTED) {
            throw new DomainIllegalStateException(DomainErrorCode.INVALID_RESERVATION_STATUS);
        }
        this.status = ReservationStatus.CANCELED;
    }
}
