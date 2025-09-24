package com.rouby.batch.notification;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import com.rouby.notification.notificationEvent.domain.sender.AsyncNotificationSender;
import com.rouby.notification.notificationEvent.domain.sender.NotificationSender;
import com.rouby.notification.notificationEvent.infrastructure.exception.NotificationEventFcmException;
import com.rouby.notification.notificationEvent.infrastructure.exception.NotificationEventFcmRetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDispatcher implements NotificationDispatcher {

  private final Clock clock;
  private final CircuitBreakerRegistry cbRegistry;

  @Qualifier("notificationSendExecutor")
  private final ThreadPoolTaskExecutor notificationSendExecutor;
  @Qualifier("resultCallbackExecutor")
  private final ThreadPoolTaskExecutor resultCallbackExecutor;
  @Qualifier("preciseTimer")
  private final ScheduledExecutorService preciseTimer;
  private final NotificationEventRepository notificationEventRepository;
  private final AsyncNotificationSender sender;
  private final NotificationSender basicSender;
  private final ResultWriter writer;

  @Value("${notification.dispatch.timeBudgetSec:50}")
  private int TIME_BUDGET_SEC;
  @Value("${notification.dispatch.leadMs:600}")
  private long LEAD_MS;
  @Value("${notification.dispatch.guardMs:500}")
  private long GUARD_MS; // 정각 0.5초 전에는 무조건 종료

  @Value("${notification.dispatch.limit:200}")
  private int LIMIT;
  @Value("${notification.dispatch.maxAttempt:3}")
  int MAX_ATTEMPT;
  @Value("${notification.dispatch.backfillMinutes:10}")
  private int BACKFILL_MIN;
  @Value("${notification.dispatch.ttl.ontimeSec:60}")
  private int ONTIME_TTL;
  @Value("${notification.dispatch.ttl.backfillSec:600}")
  private int BACKFILL_TTL;
  @Value("${notification.dispatch.lease.ontimeSec:45}")
  private int LEASE_ONTIME_SEC;
  @Value("${notification.dispatch.lease.backfillSec:30}")
  private int LEASE_BACKFILL_SEC;

  @Value("${notification.dispatch.maxConcurrent:512}")
  private int MAX_CONCURRENT;
  private Semaphore inFlight;

  @PostConstruct
  public void init() {
    this.inFlight = new Semaphore(MAX_CONCURRENT);
  }

  @Override
  public void dispatch(Instant sched) {

    // 자기치유
    notificationEventRepository.recoverExpiredLeases();

    // sched M:55
    Instant nextSched = sched.plus(1, ChronoUnit.MINUTES); // (M+1):55

    long possibleMsToNextSched = Math.max(0, Duration.between(Instant.now(clock), nextSched.minusMillis(GUARD_MS)).toMillis());
    long budgetMs = Math.max(0, Math.min(TIME_BUDGET_SEC * 1000L, possibleMsToNextSched));
    long deadlineNano = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(budgetMs);

    ontimeSendAsync(sched, deadlineNano);
    backfillSendAsync(deadlineNano);
  }

  private void ontimeSendAsync(Instant sched, long deadlineNano) {

    // 온타임: [slotStart, slotEnd)만 클레임 → 타이머 예약
    // 다음 트리거(nextSched) 직전까지만, guard 남기고 실행
    Instant slotStart = sched.truncatedTo(ChronoUnit.MINUTES).plus(1, ChronoUnit.MINUTES); // M+1:00
    Instant slotEnd   = slotStart.plus(1, ChronoUnit.MINUTES); // M+2:00
    boolean start = true;
    List<NotificationEventInfo> ontime;
    do {
      ontime = notificationEventRepository.claimSlot(slotStart, slotEnd, LIMIT, writer.workerId(), LEASE_ONTIME_SEC);
      for (NotificationEventInfo ev : ontime) {

        long delayMs = Math.max(
            0, Duration.between(Instant.now(clock),
                ev.dueAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().minusMillis(LEAD_MS)).toMillis());
        preciseTimer.schedule(() -> {
          if (!inFlight.tryAcquire()) { writer.reportFailure(ev.id()); return; }
          long sentAt = clock.millis();
          sendWithCbRetry(ev, ONTIME_TTL, sentAt, true, deadlineNano, 1, 3);
          }, delayMs, TimeUnit.MILLISECONDS);
      }

      if (start) {
        writer.requestFlush();
        start = false;
      }
    } while (!ontime.isEmpty() && System.nanoTime() < deadlineNano);
  }


  private void backfillSendAsync(long deadlineNano) {

    // 백필: 다음 트리거(nextSched) 직전까지만, guard 남기고 실행
    while (System.nanoTime() < deadlineNano) {

      List<NotificationEventInfo> late = notificationEventRepository.claimBackfill(
          BACKFILL_MIN, MAX_ATTEMPT, LIMIT, writer.workerId(), LEASE_BACKFILL_SEC);
      if (late.isEmpty()) break;

      for (NotificationEventInfo ev : late) {

        if (!inFlight.tryAcquire()) {
          writer.reportFailure(ev.id());
          continue;
        }
        long sentAt = clock.millis();
        sendWithCbRetry(ev, BACKFILL_TTL, sentAt, true, deadlineNano, 1, 3);
      }
    }
  }

  private void sendWithCbRetry(NotificationEventInfo ev, int ttl, long sentAt,
      boolean highPriority, long deadlineNano, int attempt, int maxAttempts) {

    CircuitBreaker fcmCb = cbRegistry.circuitBreaker("fcm");
    Supplier<CompletionStage<Boolean>> call =
        CircuitBreaker.decorateCompletionStage(fcmCb, () -> sender.sendAsync(ev, ttl, sentAt, highPriority));

    call.get().whenCompleteAsync((ok, ex) -> {
      try {
        boolean success = (ex == null && Boolean.TRUE.equals(ok));
        if (success) { writer.reportSuccess(ev.id(), sentAt); return; }

        if (ex instanceof CallNotPermittedException) {
          writer.reportFailure(ev.id());
          return;
        }
        if (!isTransient(ex)) {
          writer.reportFailure(ev.id());
          return;
        }
        if (attempt >= maxAttempts || System.nanoTime() >= deadlineNano) {
          writer.reportFailure(ev.id());
          return;
        }

        long backoffMs = computeBackoffMs(ex, attempt);
        long next = clock.instant().getNano() + TimeUnit.MILLISECONDS.toNanos(backoffMs);
        if (next >= deadlineNano) { writer.reportFailure(ev.id()); return; }

        preciseTimer.schedule(
            () -> sendWithCbRetry(ev, ttl, sentAt, highPriority, deadlineNano, attempt + 1, maxAttempts),
            backoffMs, TimeUnit.MILLISECONDS);
      } finally {
        inFlight.release();
      }
    }, resultCallbackExecutor);
  }

  private boolean isTransient(Throwable t) {
    Throwable root = (t.getCause() != null) ? t.getCause() : t;
    if (root instanceof NotificationEventFcmException fe) {
      int s = fe.getStatus().value();
      return s == 429 || (s >= 500 && s < 600);
    }
    return (root instanceof java.util.concurrent.TimeoutException)
        || (root instanceof reactor.netty.http.client.PrematureCloseException);
  }

  private long computeBackoffMs(Throwable t, int attempt) {
    long base = 200L, max = 2_000L;
    long exponential = Math.min(max, base << (attempt - 1));

    Long retryAfter = (t instanceof NotificationEventFcmRetryableException fe) ? Long.valueOf(fe.getRetryAfter()) : null;
    long ms = (retryAfter != null && retryAfter > 0) ? Math.max(exponential, retryAfter * 1000L) : exponential;

    long jitter = (long)(ms * 0.2);
    long delta = ThreadLocalRandom.current().nextLong(-jitter, jitter + 1);

    return Math.max(100L, ms + delta);
  }

  private void ontimeSend(Instant sched, long deadlineNano) {

    // 온타임: [slotStart, slotEnd)만 클레임 → 타이머 예약
    // 다음 트리거(nextSched) 직전까지만, guard 남기고 실행
    Instant slotStart = sched.truncatedTo(ChronoUnit.MINUTES).plus(1, ChronoUnit.MINUTES); // M+1:00
    Instant slotEnd   = slotStart.plus(1, ChronoUnit.MINUTES); // M+2:00

    List<NotificationEventInfo> ontime;
    do {
      ontime = notificationEventRepository.claimSlot(slotStart, slotEnd, LIMIT, writer.workerId(), LEASE_ONTIME_SEC);
      for (NotificationEventInfo ev : ontime) {

        long delayMs = Math.max(
            0, Duration.between(Instant.now(clock),
                ev.dueAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().minusMillis(LEAD_MS)).toMillis());
        preciseTimer.schedule(() -> {
          try {
            CompletableFuture.supplyAsync(() -> {
                      long sentAt = clock.millis();
                      return basicSender.send(ev, ONTIME_TTL, sentAt, true);
                    },
                    notificationSendExecutor)
                .whenCompleteAsync((ok, ex) -> {
                  if (ex == null && Boolean.TRUE.equals(ok)) writer.reportSuccess(ev.id(), clock.millis());
                  else writer.reportFailure(ev.id());
                }, resultCallbackExecutor);
          } catch (RejectedExecutionException rex) {
            writer.reportFailure(ev.id());
          }}, delayMs, TimeUnit.MILLISECONDS);
      }
    } while (!ontime.isEmpty() && System.nanoTime() < deadlineNano);
  }

  private void backfillSend(long deadlineNano) {

    // 백필: 다음 트리거(nextSched) 직전까지만, guard 남기고 실행
    while (System.nanoTime() < deadlineNano) {

      List<NotificationEventInfo> late = notificationEventRepository.claimBackfill(
          BACKFILL_MIN, MAX_ATTEMPT, LIMIT, writer.workerId(), LEASE_BACKFILL_SEC);
      if (late.isEmpty()) break;

      for (NotificationEventInfo ev : late) {
        CompletableFuture.supplyAsync(() -> {
                  long sentAt = clock.millis();
                  return basicSender.send(ev, BACKFILL_TTL, sentAt, true);
                },
                notificationSendExecutor)
            .whenCompleteAsync((ok, ex) -> {
              if (ex == null && Boolean.TRUE.equals(ok)) writer.reportSuccess(ev.id(), clock.millis());
              else writer.reportFailure(ev.id());
            }, resultCallbackExecutor);
      }
    }
  }
}
