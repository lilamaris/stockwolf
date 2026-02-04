package com.lilamaris.stockwolf.inventory.infrastructure.idempotency;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository extends JpaRepository<Idempotency, UUID> {
    @Query("""
            SELECT i
            FROM Idempotency i
            WHERE i.subject = :subject
                AND i.op = :op""")
    Optional<Idempotency> findBySubjectAndOp(String subject, String op);

    @Modifying
    @Query(value = """
            INSERT INTO idempotency (id, key, subject, op, command_hash, stringify_result, status)
            VALUES (:id, :key, :subject, :op, NULL, NULL, :status)
            ON CONFLICT (key) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(UUID id, String key, String subject, String op, String status);

    @Modifying
    @Query("""
            UPDATE Idempotency i
            SET i.status = 'IN_PROGRESS', i.commandHash = :hash, i.progressAt = :now
            WHERE i.key = :key
                AND i.status = 'READY'""")
    int moveToProgress(
            @Param("key") String key,
            @Param("hash") String hash,
            @Param("now") Instant now
    );

    @Modifying
    @Query("""
            UPDATE Idempotency i
            SET i.status = 'COMPLETE', stringifyResult = :stringifyResult
            WHERE i.key = :key
                AND i.status = 'IN_PROGRESS'""")
    int moveToComplete(
            @Param("key") String key,
            @Param("stringifyResult") String result
    );

    @Modifying
    @Query("""
            UPDATE Idempotency i
            SET i.status = 'FAIL'
            WHERE i.key = :key
                AND i.status = 'IN_PROGRESS'""")
    int moveToFail(
            @Param("key") String key
    );

    @Modifying
    @Query("""
            UPDATE Idempotency i
            SET i.status = 'RETRYABLE'
            WHERE i.key = :key
                AND i.status = 'IN_PROGRESS'""")
    int moveToRetryable(
            @Param("key") String key
    );

    default Optional<Idempotency> findByIdempotencyKey(IdempotencyKey key) {
        return findBySubjectAndOp(key.subject(), key.op());
    }
}
