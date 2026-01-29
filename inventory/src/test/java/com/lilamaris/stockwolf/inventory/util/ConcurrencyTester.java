package com.lilamaris.stockwolf.inventory.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConcurrencyTester<T> {
    private static final Logger log = LoggerFactory.getLogger(ConcurrencyTester.class);
    private final Integer threadCount;
    private final Function<Integer, T> workerFn;

    public ConcurrencyTester(
            Integer threadCount,
            Function<Integer, T> threadFn
    ) {
        this.threadCount = threadCount;
        this.workerFn = threadFn;
    }

    public void assertAllSuccess(WorkResult<T> result) {
        assertThat(result.successCount)
                .as("작업 성공한 스레드 개수")
                .isEqualTo(result.totalCount);

        assertThat(result.failCount)
                .as("작업 실패한 스레드 개수")
                .isZero();
    }

    public void assertExactSuccess(WorkResult<T> result, Integer expectSuccessCount) {
        assertThat(result.successCount)
                .as("작업 성공한 스레드 개수")
                .isEqualTo(expectSuccessCount);

        assertThat(result.failCount)
                .as("작업 실패한 스레드 개수")
                .isEqualTo(result.totalCount - expectSuccessCount);
    }

    public void summary(WorkResult<T> result) {
        log.info("""
                        \n작업 성공한 스레드 개수: {}
                        작업 실패한 스레드 개수: {}
                        실패 보고 예외: {}""",
                result.successCount,
                result.failCount,
                result.failResult.values().stream().map(Exception::getMessage).collect(Collectors.toSet())
        );
    }

    public WorkResult<T> run() throws InterruptedException {
        var ready = new CountDownLatch(threadCount);
        var start = new CountDownLatch(1);
        var done = new CountDownLatch(threadCount);

        var success = new AtomicInteger();
        var fail = new AtomicInteger();
        Map<Integer, T> successResult = new HashMap<>();
        Map<Integer, Exception> failResult = new HashMap<>();

        var executor = Executors.newFixedThreadPool(threadCount);

        try {
            for (int i = 0; i < threadCount; i++) {
                var threadId = i;

                executor.submit(() -> {
                    ready.countDown();
                    try {
                        start.await();
                        var fnResult = workerFn.apply(threadId);
                        successResult.put(threadId, fnResult);
                        success.incrementAndGet();
                    } catch (Exception e) {
                        log.warn("Concurrency Tester Thread {} reported exception {}: {}", threadId, e.getClass(), e.getMessage());
                        failResult.put(threadId, e);
                        fail.incrementAndGet();
                    } finally {
                        done.countDown();
                    }
                });
            }

            ready.await();
            start.countDown();
            done.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail.incrementAndGet();
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }

        var successCount = success.get();
        var failCount = fail.get();
        if (failCount > 0) {
            log.info("Thread report fail. {}", failResult);
        }

        return new WorkResult<T>(threadCount, successCount, failCount, successResult, failResult);
    }

    public record WorkResult<T>(
            Integer totalCount,
            Integer successCount,
            Integer failCount,
            Map<Integer, T> successResult,
            Map<Integer, Exception> failResult
    ) {
    }
}
