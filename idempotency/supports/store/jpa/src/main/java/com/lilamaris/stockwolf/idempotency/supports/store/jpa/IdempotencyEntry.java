package com.lilamaris.stockwolf.idempotency.supports.store.jpa;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyProcessingResult;
import com.lilamaris.stockwolf.idempotency.foundation.MappedIdempotencyContext;
import com.lilamaris.stockwolf.idempotency.foundation.store.IdempotencyProgressStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(
        name = "idempotency_entry",
        uniqueConstraints = @UniqueConstraint(columnNames = "key")
)
public class IdempotencyEntry implements IdempotencyProcessingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String key;

    private String subject;

    private String op;

    private String hash;

    private Instant progressAt;

    private String failReason;

    @Enumerated(EnumType.STRING)
    private IdempotencyProgressStatus status;

    private String stringifyResult;

    protected IdempotencyEntry() {
    }

    @Override
    public Optional<IdempotencyContext> context() {
        return Optional.ofNullable(hash)
                .map(MappedIdempotencyContext::new);
    }

    @Override
    public Optional<String> failReason() {
        return Optional.ofNullable(failReason);
    }

    @Override
    public Optional<String> stringifyResult() {
        return Optional.ofNullable(stringifyResult);
    }

    @Override
    public IdempotencyProgressStatus status() {
        return status;
    }

    @Override
    public Instant progressAt() {
        return progressAt;
    }
}
