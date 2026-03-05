package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.store.StoredEventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaStoredEventEnvelopeRepository extends JpaRepository<JpaStoredEventEnvelope, String> {
    @Query("""
            SELECT e
            FROM JpaStoredEventEnvelope e
            WHERE e.status = :status
            ORDER BY e.eventHeader.occurredAt ASC""")
    List<JpaStoredEventEnvelope> claimBatch(
            @Param("status") StoredEventStatus status,
            Pageable pageable
    );
}