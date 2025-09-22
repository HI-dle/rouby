package com.rouby.notification.notificationEvent.infrastructure.persistence;

import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.infrastructure.persistence.jdbc.NotificationEventJdbcRepository;
import com.rouby.notification.notificationEvent.infrastructure.persistence.jpa.NotificationEventJpaRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NotificationEventRepositoryImpl implements NotificationEventRepository {

  private final NotificationEventJpaRepository jpaRepository;
  private final NotificationEventJdbcRepository jdbcRepository;


  @Override
  public List<NotificationEventInfo> claimSlot(Instant slotStart, Instant slotEnd, int limit, String workerId, int leaseSecTime) {
    return jdbcRepository.claimSlot(slotStart, slotEnd, limit, workerId, leaseSecTime);
  }

  @Override
  public List<NotificationEventInfo> claimBackfill(
      int minutes, int maxAttempt, int limit, String workerId, int leaseSecTime) {
    return jdbcRepository.claimBackfill(minutes, maxAttempt, limit, workerId, leaseSecTime);
  }

  @Override
  public int markSent(List<Long> ids, String workerId) {
    return jdbcRepository.markSent(ids, workerId);
  }

  @Override
  public int markRetry(List<Long> ids, String workerId) {
    return jdbcRepository.markRetry(ids, workerId);
  }

  @Override
  public int recoverExpiredLeases() {
    return jdbcRepository.recoverExpiredLeases();
  }

  @Override
  public NotificationEvent save(NotificationEvent entity) {
    return jpaRepository.save(entity);
  }

  @Override
  public <S extends NotificationEvent> List<S> saveAll(Iterable<S> entities) {
    return jpaRepository.saveAll(entities);
  }
}
