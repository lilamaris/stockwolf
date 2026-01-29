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

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class InventoryConcurrencyTest {
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
    void 동시에_여러_예약_들어와도_재고_초과하지_않음() throws Exception {
        // given
        var skuId = "SKU-1";
        var initialQuantity = 10;
        inventoryStore.save(Inventory.create(skuId, initialQuantity, 0));

        int threadCount = 50;
        int targetReservationQty = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);


        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();

                    inventoryService.reserve(
                            UUID.randomUUID().toString(),
                            skuId,
                            targetReservationQty
                    );
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // then
        Inventory inventory = inventoryStore.get(skuId).orElseThrow();
        int expectedReservationCount = initialQuantity / targetReservationQty;

        assertThat(successCount.get()).as("예약 성공한 스레드 개수").isEqualTo(expectedReservationCount);
        assertThat(failCount.get()).as("예약 실패한 스레드 개수").isEqualTo(threadCount - expectedReservationCount);
        assertThat(inventory.getReservedQuantity()).as("제품 예약 걸린 개수").isEqualTo(initialQuantity);
    }
}
