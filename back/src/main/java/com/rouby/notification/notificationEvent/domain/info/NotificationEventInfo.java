package com.rouby.notification.notificationEvent.domain.info;

import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationMessage;
import com.rouby.notification.notificationEvent.domain.entity.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationEventInfo(
    Long id,
    Long userId,
    DeviceTokenInfo deviceTokenInfo,
    NotificationMessage message,
    NotificationType type,
    LocalDateTime dueAt
) {

  public String getTag() {
    if (type == NotificationType.FEEDBACK) {
      return "feedback-" + message.getUrl().split("/")[2];
    }
    return "evt-" + id;
  }
}
