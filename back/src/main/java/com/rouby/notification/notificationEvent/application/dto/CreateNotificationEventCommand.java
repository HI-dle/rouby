package com.rouby.notification.notificationEvent.application.dto;

import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.entity.NotificationMessage;
import com.rouby.notification.notificationEvent.domain.entity.NotificationType;
import com.rouby.notification.notificationEvent.domain.entity.TokenProviderType;
import lombok.Builder;

@Builder
public record CreateNotificationEventCommand(
    Long userId,
    String tokenProviderType,
    String deviceToken,
    String title,
    String body,
    String url,
    String notificationType
) {

  public NotificationEvent toEntity() {
    return NotificationEvent.builder()
        .userId(userId)
        .deviceTokenInfo(buildDeviceTokenInfo())
        .message(buildeNotificationMessage())
        .type(NotificationType.parse(notificationType))
        .build();
  }

  private DeviceTokenInfo buildDeviceTokenInfo() {

    return DeviceTokenInfo.builder()
        .tokenProvider(TokenProviderType.parse(tokenProviderType))
        .deviceToken(deviceToken)
        .build();
  }

  private NotificationMessage buildeNotificationMessage() {

    return NotificationMessage.builder()
        .title(title)
        .body(body)
        .url(url)
        .build();
  }
}
