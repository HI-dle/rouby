package com.rouby.notification.notificationEvent.domain.info;

import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationMessage;
import com.rouby.notification.notificationEvent.domain.entity.NotificationType;
import java.time.Instant;
import lombok.Builder;

@Builder
public record NotificationEventInfo(
    Long id,
    Long userId,
    DeviceTokenInfo deviceTokenInfo,
    NotificationMessage message,
    NotificationType type,
    Instant dueAt
) {

}
