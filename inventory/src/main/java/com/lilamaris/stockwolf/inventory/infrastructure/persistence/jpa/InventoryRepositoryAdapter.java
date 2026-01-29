package com.lilamaris.stockwolf.inventory.infrastructure.persistence.jpa;

import com.lilamaris.stockwolf.inventory.application.port.out.InventoryStore;
import com.lilamaris.stockwolf.inventory.domain.Inventory;
import com.lilamaris.stockwolf.inventory.infrastructure.persistence.jpa.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryStore {
    private final InventoryRepository repository;

    @Override
    public Optional<Inventory> get(String skuId) {
        return repository.findById(skuId);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return repository.save(inventory);
    }
}
