package com.rouby.batch.notification;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
  @Value("${notification.writer.flush.intervalMs:200}")
  long INTERVAL_MS;

  private final CircuitBreakerRegistry cbRegistry;
  @Qualifier("flushScheduler")
  private final ScheduledExecutorService flushScheduler;
  private final DbWriterAdapter dbWriterAdapter;

  private final String workerId = UUID.randomUUID().toString();

  private final BlockingQueue<Long> okQ     = new LinkedBlockingQueue<>(10_000);
  private final BlockingQueue<Long> failQ   = new LinkedBlockingQueue<>(10_000);

  public String workerId() { return workerId; }

  @PostConstruct
  void start() {
    flushScheduler.scheduleAtFixedRate(this::flushTick, INTERVAL_MS, INTERVAL_MS, TimeUnit.MILLISECONDS);
  }

  private void flushTick() {
    CircuitBreaker cb = cbRegistry.circuitBreaker("dbWriter");

    if (cb.getState() == CircuitBreaker.State.OPEN) return;
    drainAndUpdate(okQ,   true);

    if (cb.getState() == CircuitBreaker.State.OPEN) return;
    drainAndUpdate(failQ, false);
  }

  public void reportSuccess(long id) {
    try {
      if (!okQ.offer(id)) {
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

  private void drainAndUpdate(BlockingQueue<Long> q, boolean success) {

    CircuitBreaker cb = cbRegistry.circuitBreaker("dbWriter");
    int batchSize = (cb.getState() == CircuitBreaker.State.HALF_OPEN)
        ? Math.min(FLUSH_BATCH_SIZE, 50)
        : FLUSH_BATCH_SIZE;

    while (true) {
      ArrayList<Long> inFlight = new ArrayList<>(batchSize);
      q.drainTo(inFlight, batchSize);

      if (inFlight.isEmpty()) break;

      try {
        if (success) dbWriterAdapter.markSentResilient(inFlight, workerId);
        else dbWriterAdapter.markRetryResilient(inFlight, workerId);
      } catch (Exception e) {
        onDbFail(inFlight, q);
        log.warn("drainAndUpdate DB failed; requeued {} items", inFlight.size(), e);
        break;
      }
    }
  }

  private void onDbFail(List<Long> inFlight, BlockingQueue<Long> q) {
    for (Long id : inFlight) {
      if (!q.offer(id)) {
        log.error("requeue overflow: id={}", id);
      }
    }
  }

  @Override
  @PreDestroy
  public void close() {
    flushTick();
    flushScheduler.shutdown();
  }
}