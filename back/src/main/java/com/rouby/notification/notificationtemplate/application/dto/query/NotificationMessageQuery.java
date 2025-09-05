package com.rouby.notification.notificationtemplate.application.dto.query;

import com.rouby.notification.notificationtemplate.domain.entity.NotificationType;

public record NotificationMessageQuery(
    String username,
    NotificationType notificationType
) {
}
