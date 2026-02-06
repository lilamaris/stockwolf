package com.lilamaris.stockwolf.idempotency.supports.store.jpa;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.Idempotent;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyStore;
import com.lilamaris.stockwolf.idempotency.foundation.store.IdempotencyProgressStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IdempotencyJpaStore implements IdempotencyStore {
    private final IdempotencyRepository repository;
    private final ObjectMapper mapper;

    public IdempotencyJpaStore(
            IdempotencyRepository repository,
            ObjectMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Idempotent ensureExistAndGet(
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
    public <T> Optional<T> resolveResult(Idempotent idempotent, Class<T> expect) {
        return idempotent.rawResult()
                .map(o -> mapper.convertValue(o, expect));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean progress(IdempotencyKey key, IdempotencyContext context) {
        var idemKey = getIdempotencyKey(key);
        return repository.moveToProgress(idemKey, context.hash(), Instant.now()) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean complete(IdempotencyKey key, Object result) {
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
