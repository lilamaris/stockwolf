package com.lilamaris.stockwolf.inventory.application;

import com.lilamaris.stockwolf.inventory.application.port.out.InventoryStore;
import com.lilamaris.stockwolf.inventory.application.service.InventoryService;
import com.lilamaris.stockwolf.inventory.domain.Inventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class InventoryIdempotencyTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:18-alpine"
    )
            .withDatabaseName("inventory_test")
            .withUsername("test_user")
            .withPassword("test_password");
    @Autowired
    InventoryService inventoryService;
    @Autowired
    InventoryStore inventoryStore;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void 같은_correlationId는_한번만_예약된다() throws Exception {
        String skuId = "SKU-1";
        inventoryStore.save(Inventory.create(skuId, 5, 0));

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    inventoryService.reserve(
                            "ORDER-1",
                            skuId,
                            1
                    );
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Inventory inventory = inventoryStore.get(skuId).get();
        assertThat(inventory.getReservedQuantity()).isEqualTo(1);
    }
}
