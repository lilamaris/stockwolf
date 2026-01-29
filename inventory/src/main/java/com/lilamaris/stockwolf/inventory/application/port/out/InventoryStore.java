package com.lilamaris.stockwolf.inventory.application.port.out;

import com.lilamaris.stockwolf.inventory.domain.Inventory;

import java.util.Optional;

public interface InventoryStore {
    Optional<Inventory> get(String skuId);

    Inventory save(Inventory inventory);
}
