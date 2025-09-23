package com.rouby.batch.notification;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import com.rouby.notification.notificationEvent.domain.sender.AsyncNotificationSender;
import com.rouby.notification.notificationEvent.domain.sender.NotificationSender;
import jakarta.annotation.PostConstruct;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
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
          sender.sendAsync(ev, ONTIME_TTL, sentAt, true)
              .whenComplete((ok, ex) -> {
                try {
                  if (ex == null && Boolean.TRUE.equals(ok)) writer.reportSuccess(ev.id(), sentAt);
                  else {
                    writer.reportFailure(ev.id());
                    if (ex != null) {
                      log.error("fcm 알림 발송 처리 실패", ex);
                    } else {
                      log.warn("fcm 알림 발송 처리 실패: 응답은 false (예외 없음)");
                    }
                  }
                } finally {
                  inFlight.release();
                }
              });
          }, delayMs, TimeUnit.MILLISECONDS);
      }

      if (start) {
        writer.requestFlush();
        start = false;
      }
    } while (!ontime.isEmpty() && System.nanoTime() < deadlineNano);
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
        sender.sendAsync(ev, BACKFILL_TTL, sentAt, true)
            .whenComplete((ok, ex) -> {
              try {
                if (ex == null && Boolean.TRUE.equals(ok)) writer.reportSuccess(ev.id(), sentAt);
                else {
                  writer.reportFailure(ev.id());
                  if (ex != null) {
                    log.error("fcm 알림 발송 처리 실패", ex);
                  } else {
                    log.warn("fcm 알림 발송 처리 실패: 응답은 false (예외 없음)");
                  }
                }
              } finally {
                inFlight.release();
              }
            });
      }
    }
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
