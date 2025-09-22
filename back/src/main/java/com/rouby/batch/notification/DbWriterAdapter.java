package com.rouby.batch.notification;

import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbWriterAdapter {

  private final NotificationEventRepository repo;

  @CircuitBreaker(name = "dbWriter")
  @Retry(name = "dbWriter")
  public void markSentResilient(java.util.List<Long> ids, String workerId) {
    repo.markSent(ids, workerId);
  }

  @CircuitBreaker(name = "dbWriter")
  @Retry(name = "dbWriter")
  public void markRetryResilient(java.util.List<Long> ids, String workerId) {
    repo.markRetry(ids, workerId);
  }
}