package com.rouby.notification.notificationEvent.presentation.dto;

import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventWithTemplateCommand;

public record CreateNotificationEventRequest(
    Long userId,
    String tokenProviderType,
    String deviceToken,
    String title,
    String body,
    String url,
    String notificationType
) {

  public CreateNotificationEventWithTemplateCommand toCommand() {
    return CreateNotificationEventWithTemplateCommand.builder()
        .userId(userId)
        .tokenProviderType(tokenProviderType)
        .deviceToken(deviceToken)
        .title(title)
        .body(body)
        .url(url)
        .notificationType(notificationType)
        .build();
  }
}
