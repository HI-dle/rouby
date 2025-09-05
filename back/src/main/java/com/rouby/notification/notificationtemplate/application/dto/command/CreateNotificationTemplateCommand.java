package com.rouby.notification.notificationtemplate.application.dto.command;

import com.rouby.notification.notificationtemplate.domain.entity.Message;
import com.rouby.notification.notificationtemplate.domain.entity.NotificationTemplate;
import com.rouby.notification.notificationtemplate.domain.entity.NotificationType;

public record CreateNotificationTemplateCommand(
    NotificationType notificationType,
    Message message
) {

  public NotificationTemplate toEntity() {
    return NotificationTemplate.builder()
        .type(notificationType)
        .message(message)
        .build();
  }
}
