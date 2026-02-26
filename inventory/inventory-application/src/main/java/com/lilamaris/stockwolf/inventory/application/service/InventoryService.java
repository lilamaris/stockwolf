package com.lilamaris.stockwolf.inventory.application.service;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyExecutor;
import com.lilamaris.stockwolf.idempotency.foundation.MappedIdempotencyContext;
import com.lilamaris.stockwolf.idempotency.foundation.MappedIdempotencyKey;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationEntry;
import com.lilamaris.stockwolf.inventory.application.port.in.ReservationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements
        ReservationManager {
    private final TxService tx;

    private final IdempotencyExecutor idempotencyExecutor;

    @Override
    public ReservationEntry reserve(
            String correlationId,
            String skuId,
            int quantity
    ) {
        var idemKey = new MappedIdempotencyKey(correlationId, "reserve");
        var idemCtx = MappedIdempotencyContext.from(skuId, quantity);
        var execute = idempotencyExecutor.execute(
                idemKey,
                idemCtx,
                () -> tx.tryReserve(correlationId, skuId, quantity),
                ReservationEntry.class
        );
        return execute.result();
    }

    @Override
    public ReservationEntry commit(String correlationId) {
        var idemKey = new MappedIdempotencyKey(correlationId, "commit");
        var idemCtx = MappedIdempotencyContext.noContext();
        var execute = idempotencyExecutor.execute(
                idemKey,
                idemCtx,
                () -> tx.tryCommit(correlationId),
                ReservationEntry.class
        );
        return execute.result();
    }

    @Override
    public ReservationEntry cancel(String correlationId) {
        var idemKey = new MappedIdempotencyKey(correlationId, "cancel");
        var idemCtx = MappedIdempotencyContext.noContext();
        var execute = idempotencyExecutor.execute(
                idemKey,
                idemCtx,
                () -> tx.tryCancel(correlationId),
                ReservationEntry.class
        );
        return execute.result();
    }
}
