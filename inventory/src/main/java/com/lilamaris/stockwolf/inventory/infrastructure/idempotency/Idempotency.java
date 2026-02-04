package com.lilamaris.stockwolf.inventory.infrastructure.idempotency;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyProcessingResult;
import com.lilamaris.stockwolf.idempotency.foundation.MappedIdempotencyContext;
import com.lilamaris.stockwolf.idempotency.foundation.store.IdempotencyProgressStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(
        name = "idempotency",
        uniqueConstraints = @UniqueConstraint(columnNames = "key")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idempotency implements IdempotencyProcessingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String key;

    private String subject;

    private String op;

    private String commandHash;

    private Instant progressAt;

    private String failReason;

    @Enumerated(EnumType.STRING)
    private IdempotencyProgressStatus status;

    @Setter
    private String stringifyResult;

    @Override
    public Optional<IdempotencyContext> context() {
        return Optional.ofNullable(commandHash)
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
