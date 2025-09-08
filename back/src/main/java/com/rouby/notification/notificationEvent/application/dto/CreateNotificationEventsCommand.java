package com.rouby.notification.notificationEvent.application.dto;

import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.entity.NotificationMessage;
import com.rouby.notification.notificationEvent.domain.entity.NotificationType;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateNotificationEventsCommand(
    Long userId,
    List<DeviceTokenInfo> deviceTokenInfos,
    String title,
    String body,
    String url,
    NotificationType notificationType
) {

  public List<NotificationEvent> toEntities() {
    return deviceTokenInfos.stream()
        .map(deviceTokenInfo -> NotificationEvent.builder()
            .userId(userId)
            .deviceTokenInfo(deviceTokenInfo)
            .message(buildNotificationMessage())
            .type(notificationType)
            .build())
        .toList();
  }

  private NotificationMessage buildNotificationMessage() {
    return NotificationMessage.builder()
        .title(title)
        .body(body)
        .url(url)
        .build();
  }
}
