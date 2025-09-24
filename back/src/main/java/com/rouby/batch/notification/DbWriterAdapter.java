package com.rouby.batch.notification;

import com.rouby.notification.notificationEvent.domain.info.SuccessResult;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbWriterAdapter {

  private final NotificationEventRepository repo;

  @CircuitBreaker(name = "dbWriter")
  public int markSentResilient(List<SuccessResult> successResults, String workerId) {
    if (successResults == null || successResults.isEmpty()) return 0;
    return repo.markSent(successResults, workerId);
  }

  @CircuitBreaker(name = "dbWriter")
  public int markRetryResilient(List<Long> ids, String workerId) {
    if (ids == null || ids.isEmpty()) return 0;
    return repo.markRetry(ids, workerId);
  }
}