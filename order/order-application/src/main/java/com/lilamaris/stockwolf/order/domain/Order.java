package com.lilamaris.stockwolf.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "correlation_id", nullable = false, updatable = false)
    private String correlationId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private int totalAmount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Order create(
            String correlationId,
            String userId
    ) {
        var order = new Order();
        order.correlationId = correlationId;
        order.userId = userId;
        order.status = OrderStatus.CREATED;
        return order;
    }

    public void addItem(
            String productId,
            int quantity,
            int unitPrice
    ) {
        var item = OrderItem.create(
                this,
                productId,
                quantity,
                unitPrice
        );

        this.items.add(item);
    }

    public void markInventoryProcess(boolean success) {
        requireStatus(OrderStatus.CREATED);
        this.status = success ? OrderStatus.INVENTORY_RESERVED : OrderStatus.INVENTORY_FAILED;
        this.updatedAt = Instant.now();
    }

    public void markPaymentProcess(boolean success) {
        requireStatus(OrderStatus.PAYMENT_REQUESTED);
        this.status = success ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED;
        this.updatedAt = Instant.now();
    }

    public void requestPayment() {
        requireStatus(OrderStatus.INVENTORY_RESERVED);
        this.status = OrderStatus.PAYMENT_REQUESTED;
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        // 정책에 맞게 확장
        if (status == OrderStatus.PAID) throw new IllegalStateException("Already paid");
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    private void requireStatus(OrderStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException("Invalid transition: " + status + " -> " + expected);
        }
    }
}
