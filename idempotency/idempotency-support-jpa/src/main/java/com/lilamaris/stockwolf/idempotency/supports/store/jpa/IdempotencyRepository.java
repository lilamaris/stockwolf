package com.lilamaris.stockwolf.idempotency.supports.store.jpa;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository extends JpaRepository<IdempotencyEntry, UUID> {
    @Query("""
            SELECT i
            FROM IdempotencyEntry i
            WHERE i.subject = :subject
                AND i.op = :op""")
    Optional<IdempotencyEntry> findBySubjectAndOp(String subject, String op);

    @Modifying
    @Query(value = """
            INSERT INTO idempotency_entry (id, key, subject, op, hash, result, status)
            VALUES (:id, :key, :subject, :op, NULL, NULL, :status)
            ON CONFLICT (key) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(UUID id, String key, String subject, String op, String status);

    @Modifying
    @Query("""
            UPDATE IdempotencyEntry i
            SET i.status = 'IN_PROGRESS', i.hash = :hash, i.progressAt = :now
            WHERE i.key = :key
                AND i.status = 'READY'""")
    int moveToProgress(
            @Param("key") String key,
            @Param("hash") String hash,
            @Param("now") Instant now
    );

    @Modifying
    @Query("""
            UPDATE IdempotencyEntry i
            SET i.status = 'COMPLETE', result = :result
            WHERE i.key = :key
                AND i.status = 'IN_PROGRESS'""")
    int moveToComplete(
            @Param("key") String key,
            @Param("result") Object result
    );

    @Modifying
    @Query("""
            UPDATE IdempotencyEntry i
            SET i.status = 'FAIL'
            WHERE i.key = :key
                AND i.status = 'IN_PROGRESS'""")
    int moveToFail(
            @Param("key") String key
    );

    @Modifying
    @Query("""
            UPDATE IdempotencyEntry i
            SET i.status = 'RETRYABLE'
            WHERE i.key = :key
                AND i.status = 'IN_PROGRESS'""")
    int moveToRetryable(
            @Param("key") String key
    );

    default Optional<IdempotencyEntry> findByIdempotencyKey(IdempotencyKey key) {
        return findBySubjectAndOp(key.subject(), key.op());
    }
}
