package com.rouby.notification.notificationEvent.application.dto;

import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.entity.NotificationMessage;
import com.rouby.notification.notificationEvent.domain.entity.NotificationType;
import com.rouby.notification.notificationEvent.domain.entity.SendStatus;
import com.rouby.notification.notificationEvent.domain.entity.TokenProviderType;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationtemplate.application.dto.MessageInfo;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateNotificationEventWithTemplateCommand(
    Long userId,
    String tokenProviderType,
    String deviceToken,
    String title,
    String body,
    String url,
    String notificationType
) {

  public static CreateNotificationEventWithTemplateCommand from(
      CreateNotificationEventCommand command, MessageInfo messageInfo) {

    return CreateNotificationEventWithTemplateCommand.builder()
        .userId(command.userId())
        .tokenProviderType(command.tokenProviderType())
        .deviceToken(command.deviceToken())
        .url(command.url())
        .title(messageInfo.title())
        .body(messageInfo.body())
        .notificationType(command.notificationType())
        .build();
  }

  public NotificationEvent toEntity(Boolean notificationEnabled) {
    return NotificationEvent.builder()
        .userId(userId)
        .deviceTokenInfo(buildDeviceTokenInfo())
        .message(buildeNotificationMessage())
        .type(NotificationType.parse(notificationType))
        .status(notificationEnabled ? SendStatus.PENDING : SendStatus.SKIPPED)
        .dueAt(LocalDateTime.now().plusSeconds(1))
        .build();
  }


  public NotificationEventInfo toInfo() {

    return NotificationEventInfo.builder()
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
