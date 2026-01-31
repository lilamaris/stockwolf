package com.lilamaris.stockwolf.inventory.domain;

import com.lilamaris.stockwolf.inventory.domain.exception.DomainErrorCode;
import com.lilamaris.stockwolf.kernel.foundation.DomainIllegalStateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "inventory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Inventory {
    @Id
    @Column(name = "sku_id", nullable = false, updatable = false)
    private String skuId;

    private int totalQuantity;

    private int reservedQuantity;

    @Version
    private Long version;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedDate
    private Instant createdAt;

    public static Inventory create(
            String skuId,
            int totalQuantity,
            int reservedQuantity
    ) {

        var inventory = new Inventory();
        inventory.skuId = skuId;
        inventory.totalQuantity = totalQuantity;
        inventory.reservedQuantity = reservedQuantity;
        return inventory;
    }

    public int getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }

    public void reserve(int quantity) {
        if (getAvailableQuantity() < quantity) {
            throw new DomainIllegalStateException(DomainErrorCode.INVALID_RESERVE_QUANTITY);
        }
        this.reservedQuantity += quantity;
    }

    public void release(int quantity) {
        if (this.reservedQuantity < quantity) {
            throw new DomainIllegalStateException(DomainErrorCode.INVALID_RELEASE_QUANTITY);
        }
        this.reservedQuantity -= quantity;
    }

    public void commit(int quantity) {
        if (this.reservedQuantity < quantity) {
            throw new DomainIllegalStateException(DomainErrorCode.INVALID_COMMIT_QUANTITY);
        }
        this.reservedQuantity -= quantity;
        this.totalQuantity -= quantity;
    }
}
