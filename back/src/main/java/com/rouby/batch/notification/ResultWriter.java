package com.rouby.batch.notification;

import com.rouby.notification.notificationEvent.domain.info.SuccessResult;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultWriter implements AutoCloseable {

  @Value("${notification.writer.flush.batch:500}")
  int FLUSH_BATCH_SIZE;
  @Value("${notification.writer.flush.intervalMs:1000}")
  long INTERVAL_MS;

  private final CircuitBreakerRegistry cbRegistry;
  @Qualifier("flushScheduler")
  private final ScheduledExecutorService flushScheduler;
  private final DbWriterAdapter dbWriterAdapter;

  private final String workerId = UUID.randomUUID().toString();
  private final AtomicBoolean wakeupScheduled = new AtomicBoolean(false);
  private final BlockingQueue<SuccessResult> okQ = new LinkedBlockingQueue<>(10_000);
  private final BlockingQueue<Long> failQ = new LinkedBlockingQueue<>(10_000);

  public String workerId() { return workerId; }

  @PostConstruct
  public void start() {
    flushScheduler.scheduleWithFixedDelay(this::flushTick, INTERVAL_MS, INTERVAL_MS, TimeUnit.MILLISECONDS);
  }

  public void requestFlush() {

    if (wakeupScheduled.compareAndSet(false, true)) {
      flushScheduler.execute(() -> {
        try {
          safeFlushTick();
        } finally {
          wakeupScheduled.set(false);
        }
      });
    }
  }

  private void safeFlushTick() {
    try {
      flushTick();
    } catch (Throwable t) {
      log.error("flushTick failed", t);
    }
  }

  private void flushTick() {
    CircuitBreaker cb = cbRegistry.circuitBreaker("dbWriter");

    if (cb.getState() == CircuitBreaker.State.OPEN) return;
    drainAndUpdate(okQ,
        batch -> dbWriterAdapter.markSentResilient(batch, workerId),
        this::onDbFail);
    drainAndUpdate(failQ,
        batch -> dbWriterAdapter.markRetryResilient(batch, workerId),
        this::onDbFail);
  }

  public void reportSuccess(long id, long sentAt) {

    try {
      if (!okQ.offer(new SuccessResult(id, sentAt))) {
        int size = okQ.size(), rem = okQ.remainingCapacity();
        log.atError()
            .addKeyValue("queue", "okQ")
            .addKeyValue("eventId", id)
            .addKeyValue("size", size)
            .addKeyValue("remaining", rem)
            .addKeyValue("capacity", size + rem)
            .log("enqueue overflow: dropped");
      }
    } catch (Exception e) {
      log.atError()
          .addKeyValue("queue", "okQ")
          .addKeyValue("eventId", id)
          .addKeyValue("size", okQ.size())
          .addKeyValue("remaining", okQ.remainingCapacity())
          .setCause(e)
          .log("enqueue error");
    }
  }
  public void reportFailure(long id) {

    try {
      if (!failQ.offer(id)) {
        int size = failQ.size(), rem = failQ.remainingCapacity();
        log.atError()
            .addKeyValue("queue", "failQ")
            .addKeyValue("eventId", id)
            .addKeyValue("size", size)
            .addKeyValue("remaining", rem)
            .addKeyValue("capacity", size + rem)
            .log("enqueue overflow: dropped");
      }
    } catch (Exception e) {
      log.atError()
          .addKeyValue("queue", "failQ")
          .addKeyValue("eventId", id)
          .addKeyValue("size", failQ.size())
          .addKeyValue("remaining", failQ.remainingCapacity())
          .setCause(e)
          .log("enqueue error");
    }
  }

  private <E> void drainAndUpdate(BlockingQueue<E> q,
      ThrowingConsumer<List<E>> process, BiConsumer<List<E>, BlockingQueue<E>> onFail) {

    CircuitBreaker cb = cbRegistry.circuitBreaker("dbWriter");

    while (true) {
      CircuitBreaker.State state = cb.getState();
      if (state == CircuitBreaker.State.OPEN) {
        break;
      }
      int batchSize = (state == CircuitBreaker.State.HALF_OPEN)
          ? Math.min(FLUSH_BATCH_SIZE, 50)
          : FLUSH_BATCH_SIZE;

      ArrayList<E> inFlight = new ArrayList<>(batchSize);
      q.drainTo(inFlight, batchSize);
      if (inFlight.isEmpty()) break;

      try {
        process.accpet(inFlight);
        if (state == CircuitBreaker.State.HALF_OPEN) break;
      } catch (Exception e) {
        onFail.accept(inFlight, q);
        log.warn("drainAndUpdate DB failed; requeued {} items", inFlight.size(), e);
        break;
      }
    }
  }

  private <E> void onDbFail(List<E> inFlight, BlockingQueue<E> q) {
    for (E id : inFlight) {
      if (!q.offer(id)) {
        log.error("requeue overflow: id={}", id);
      }
    }
  }

  @Override
  @PreDestroy
  public void close() {

    flushScheduler.shutdown();
    boolean terminated = false;
    try {
      terminated = flushScheduler.awaitTermination(INTERVAL_MS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }

    final long budgetNanos = TimeUnit.MILLISECONDS.toNanos(500);
    final long deadline = System.nanoTime() + budgetNanos;

    while ((!(okQ.isEmpty() && failQ.isEmpty())) && System.nanoTime() < deadline) {
      try {
        flushTick();
      } catch (Throwable t) {
        log.warn("서버 종료를 위한 알림 결과 작성 스케쥴러 정리 중 오류가 발생하였습니다.", t);
        break;
      }
    }

    int okLeft = okQ.size(), failLeft = failQ.size();
    if (okLeft + failLeft > 0) {
      log.warn("종료 시 미처리 항목: okQ={}, failQ={}", okLeft, failLeft);
    }

    if (!terminated) {
      flushScheduler.shutdownNow();
    }
  }

  @FunctionalInterface
  interface ThrowingConsumer<T> {
    void accpet(T t) throws Exception;
  }
}