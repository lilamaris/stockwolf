package com.lilamaris.stockwolf.event.support.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEnvelope, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from OutboxEventEnvelope e where e.status = 'PENDING' order by e.eventHeader.occurredAt")
    List<OutboxEventEnvelope> findPending(Pageable pageable);
}
