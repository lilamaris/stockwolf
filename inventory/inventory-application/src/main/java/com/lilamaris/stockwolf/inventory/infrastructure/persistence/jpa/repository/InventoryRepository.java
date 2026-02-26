package com.lilamaris.stockwolf.inventory.infrastructure.persistence.jpa.repository;

import com.lilamaris.stockwolf.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
}
