package com.lilamaris.stockwolf.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productId;

    private int quantity;

    private int unitPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    protected static OrderItem create(
            Order order,
            String productId,
            int quantity,
            int unitPrice
    ) {
        var orderItem = new OrderItem();
        orderItem.order = order;
        orderItem.productId = productId;
        orderItem.quantity = quantity;
        orderItem.unitPrice = unitPrice;
        return orderItem;
    }
}