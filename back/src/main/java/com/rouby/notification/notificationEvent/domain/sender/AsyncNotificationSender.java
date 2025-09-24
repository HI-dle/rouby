package com.rouby.notification.notificationEvent.domain.sender;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import java.util.concurrent.CompletableFuture;

public interface AsyncNotificationSender {

  CompletableFuture<Boolean> sendAsync(
      NotificationEventInfo ev, int ttlSec, long sentAt, boolean highPriority);
}
