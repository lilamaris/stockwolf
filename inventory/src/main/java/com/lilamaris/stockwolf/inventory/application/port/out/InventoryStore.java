package com.lilamaris.stockwolf.inventory.application.port.out;

import com.lilamaris.stockwolf.inventory.domain.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryStore {
    Optional<Inventory> get(String skuId);

    List<Inventory> getAll(List<String> skuIds);

    Inventory save(Inventory inventory);

    List<Inventory> saveAll(List<Inventory> inventories);
}
