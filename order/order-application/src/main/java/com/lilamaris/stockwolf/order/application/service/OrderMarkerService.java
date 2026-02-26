package com.lilamaris.stockwolf.order.application.service;

import com.lilamaris.stockwolf.order.application.exception.ApplicationErrorCode;
import com.lilamaris.stockwolf.order.application.exception.ApplicationResourceNotFoundException;
import com.lilamaris.stockwolf.order.application.port.in.OrderEntry;
import com.lilamaris.stockwolf.order.application.port.in.OrderMarker;
import com.lilamaris.stockwolf.order.application.port.out.OrderStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderMarkerService implements OrderMarker {
    private final OrderStore orderStore;

    @Override
    @Transactional
    public OrderEntry markInventoryProcess(String correlationId, boolean success) {
        var order = orderStore.getByCorrelationId(correlationId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.ORDER_NOT_FOUND));

        order.markInventoryProcess(success);

        return OrderEntry.from(order);
    }

    @Override
    @Transactional
    public OrderEntry markPaymentProcess(String correlationId, boolean success) {
        var order = orderStore.getByCorrelationId(correlationId)
                .orElseThrow(() -> new ApplicationResourceNotFoundException(ApplicationErrorCode.ORDER_NOT_FOUND));

        order.markPaymentProcess(success);

        return OrderEntry.from(order);
    }
}
