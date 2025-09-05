package com.rouby.notification.notificationtemplate.application.dto.command;

import com.rouby.notification.notificationtemplate.domain.entity.Message;
import com.rouby.notification.notificationtemplate.domain.entity.NotificationTemplate;
import com.rouby.notification.notificationtemplate.domain.entity.NotificationType;

public record CreateNotificationTemplateCommand(
    NotificationType notificationType,
    String title,
    String body
) {

  public NotificationTemplate toEntity() {
    return NotificationTemplate.builder()
        .type(notificationType)
        .message(Message.of(title, body))
        .build();
  }

}
