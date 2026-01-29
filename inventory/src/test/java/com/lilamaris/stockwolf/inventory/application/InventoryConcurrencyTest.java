package com.lilamaris.stockwolf.inventory.application;

import com.lilamaris.stockwolf.inventory.application.port.out.InventoryStore;
import com.lilamaris.stockwolf.inventory.application.port.out.ReservationStore;
import com.lilamaris.stockwolf.inventory.application.service.InventoryService;
import com.lilamaris.stockwolf.inventory.domain.Inventory;
import com.lilamaris.stockwolf.inventory.util.ConcurrencyTester;
import com.lilamaris.stockwolf.inventory.util.SequentialIdentifyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class InventoryConcurrencyTest {
    private static final Logger log = LoggerFactory.getLogger(InventoryConcurrencyTest.class);
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:18-alpine"
    )
            .withDatabaseName("inventory_test")
            .withUsername("test_user")
            .withPassword("test_password");
    static SequentialIdentifyGenerator skuIdGenerator = new SequentialIdentifyGenerator("SKU");
    static SequentialIdentifyGenerator correlationIdGenerator = new SequentialIdentifyGenerator("ORDER");
    @Autowired
    InventoryService inventoryService;
    @Autowired
    InventoryStore inventoryStore;
    @Autowired
    ReservationStore reservationStore;

    int threadCount;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void run() {
        this.threadCount = 5000;
    }

    @Test
    void 동시에_여러_예약_들어와도_재고_초과하지_않음() throws Exception {
        // given
        var initialQty = 10;
        var initialRsvQty = 0;
        var rsvQty = 2;

        var skuId = initializeInventory(initialQty, initialRsvQty);

        var tester = new ConcurrencyTester<>(
                threadCount,
                (id) -> inventoryService.reserve(correlationIdGenerator.generate(), skuId, rsvQty)
        );

        // then
        var result = tester.run();
        tester.summary(result);
        var expectedReservationCount = initialQty / rsvQty;
        tester.assertExactSuccess(result, expectedReservationCount);

        Inventory inventory = inventoryStore.get(skuId).orElseThrow();
        assertThat(inventory.getReservedQuantity())
                .as("예약 걸린 개수")
                .isEqualTo(initialQty);

        assertThat(result.successResult().size())
                .as("생성된 예약 개수")
                .isEqualTo(expectedReservationCount);
    }

    @Test
    void 동시에_같은_correlationId으로_reserve해도_한_번만_예약된다() throws Exception {
        // given
        var initialQty = 10;
        var initialRsvQty = 0;
        var rsvQty = 1;

        var skuId = initializeInventory(initialQty, initialRsvQty);
        var correlationId = correlationIdGenerator.generate();

        // when
        var tester = new ConcurrencyTester<>(
                threadCount,
                (id) -> inventoryService.reserve(correlationId, skuId, rsvQty)
        );

        // then
        var result = tester.run();
        tester.summary(result);
        tester.assertAllSuccess(result);

        Inventory inventory = inventoryStore.get(skuId).get();
        assertThat(inventory.getReservedQuantity())
                .as("예약 걸린 개수")
                .isEqualTo(1);
    }

    @Test
    void 동시에_같은_correlationId으로_commit해도_재고는_한_번만_차감된다() throws Exception {
        // given
        var initialQty = 10;
        var initialRsvQty = 0;
        var rsvQty = 1;

        var skuId = initializeInventory(initialQty, initialRsvQty);
        var correlationId = initializeReservation(skuId, rsvQty);

        // when
        var tester = new ConcurrencyTester<>(
                threadCount,
                (id) -> inventoryService.commit(correlationId)
        );

        // then
        var result = tester.run();
        tester.summary(result);
        tester.assertAllSuccess(result);

        var inventory = inventoryStore.get(skuId).get();
        assertThat(inventory.getReservedQuantity()).isZero();
        assertThat(inventory.getTotalQuantity()).isEqualTo(initialQty - 1);
    }

    @Test
    void 동시에_같은_correlationId으로_cancel해도_재고는_한_번만_가산된다() throws Exception {
        // given
        var initialQty = 10;
        var initialRsvQty = 0;
        var rsvQty = 5;

        var skuId = initializeInventory(initialQty, initialRsvQty);
        var correlationId = initializeReservation(skuId, rsvQty);

        var tester = new ConcurrencyTester<>(
                threadCount,
                (id) -> inventoryService.cancel(correlationId)
        );

        // then
        var result = tester.run();
        tester.summary(result);
        tester.assertAllSuccess(result);

        var inventory = inventoryStore.get(skuId).get();
        assertThat(inventory.getReservedQuantity()).isZero();
        assertThat(inventory.getTotalQuantity()).isEqualTo(initialQty);
    }

    private String initializeInventory(int initialQty, int initialReservedQty) {
        var skuId = skuIdGenerator.generate();
        var inventory = Inventory.create(skuId, initialQty, initialReservedQty);
        inventoryStore.save(inventory);
        return skuId;
    }

    private String initializeReservation(String skuId, int reserveQty) {
        var correlationId = correlationIdGenerator.generate();
        inventoryService.reserve(correlationId, skuId, reserveQty);
        return correlationId;
    }
}