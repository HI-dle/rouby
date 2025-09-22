package com.rouby.notification.notificationEvent.domain.repository;

import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import java.util.List;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import java.time.Instant;

public interface NotificationEventRepository {

  List<NotificationEventInfo> claimSlot(Instant slotStart, Instant slotEnd, int limit, String workerId, int leaseSecTime);

  List<NotificationEventInfo> claimBackfill(int minutes, int maxAttempt, int limit, String workerId, int leaseSecTime);

  int markSent(List<Long> ids, String workerId);

  int markRetry(List<Long> ids, String workerId);

  int recoverExpiredLeases();

  NotificationEvent save(NotificationEvent entity);

  <S extends NotificationEvent> List<S> saveAll(Iterable<S> entities);
}
