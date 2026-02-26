package com.lilamaris.stockwolf.idempotency.supports.store.jpa;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.Idempotent;
import com.lilamaris.stockwolf.idempotency.foundation.MappedIdempotencyContext;
import com.lilamaris.stockwolf.idempotency.foundation.MappedIdempotencyKey;
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
public class IdempotencyEntry implements Idempotent {
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

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "text")
    private Object result;

    protected IdempotencyEntry() {
    }

    @Override
    public IdempotencyKey key() {
        return new MappedIdempotencyKey(subject, op);
    }

    @Override
    public Optional<IdempotencyContext> context() {
        return Optional.ofNullable(hash)
                .map(MappedIdempotencyContext::new);
    }

    @Override
    public Optional<Object> rawResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<String> failReason() {
        return Optional.ofNullable(failReason);
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
