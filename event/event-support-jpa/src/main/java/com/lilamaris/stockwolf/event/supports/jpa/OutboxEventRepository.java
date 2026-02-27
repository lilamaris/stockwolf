package com.lilamaris.stockwolf.event.supports.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntry, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from OutboxEventEntry e where e.status = 'PENDING' order by e.occurredAt")
    List<OutboxEventEntry> findPending();
}
