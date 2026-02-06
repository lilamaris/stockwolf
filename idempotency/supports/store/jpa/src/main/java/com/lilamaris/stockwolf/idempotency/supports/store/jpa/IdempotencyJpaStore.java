package com.lilamaris.stockwolf.idempotency.supports.store.jpa;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyProcessingResult;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyStore;
import com.lilamaris.stockwolf.idempotency.foundation.store.IdempotencyProgressStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Repository
public class IdempotencyJpaStore implements IdempotencyStore {
    private final IdempotencyRepository repository;

    public IdempotencyJpaStore(
            IdempotencyRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IdempotencyProcessingResult ensureExistAndGet(
            IdempotencyKey key
    ) {
        var idemKey = getIdempotencyKey(key);

        int affectedRaw = repository.insertIfAbsent(
                UUID.randomUUID(),
                idemKey,
                key.subject(),
                key.op(),
                IdempotencyProgressStatus.READY.name()
        );

        return repository.findByIdempotencyKey(key).orElseThrow();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean progress(IdempotencyKey key, IdempotencyContext context) {
        var idemKey = getIdempotencyKey(key);
        return repository.moveToProgress(idemKey, context.hash(), Instant.now()) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean complete(IdempotencyKey key, String result) {
        var idemKey = getIdempotencyKey(key);
        return repository.moveToComplete(idemKey, result) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean fail(IdempotencyKey key) {
        var idemKey = getIdempotencyKey(key);
        return repository.moveToFail(idemKey) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean retryable(IdempotencyKey key) {
        var idemKey = getIdempotencyKey(key);
        return repository.moveToRetryable(idemKey) == 1;
    }

    private String getIdempotencyKey(IdempotencyKey key) {
        return key.subject() + ":" + key.op();
    }
}
