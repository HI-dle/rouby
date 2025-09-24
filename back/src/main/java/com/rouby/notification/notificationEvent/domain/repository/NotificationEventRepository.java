package com.rouby.notification.notificationEvent.domain.repository;

import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.info.SuccessResult;
import java.time.Instant;
import java.util.List;

public interface NotificationEventRepository {

  List<NotificationEventInfo> claimSlot(Instant slotStart, Instant slotEnd, int limit, String workerId, int leaseSeconds);

  List<NotificationEventInfo> claimBackfill(int minutes, int maxAttempt, int limit, String workerId, int leaseSeconds);

  int markSent(List<SuccessResult> ids, String workerId);

  int markRetry(List<Long> ids, String workerId);

  int recoverExpiredLeases();

  NotificationEvent save(NotificationEvent entity);

  <S extends NotificationEvent> List<S> saveAll(Iterable<S> entities);
}
