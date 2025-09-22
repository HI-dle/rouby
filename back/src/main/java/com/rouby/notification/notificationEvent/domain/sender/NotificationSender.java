package com.rouby.notification.notificationEvent.domain.sender;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;

public interface NotificationSender {

  boolean send(NotificationEventInfo eventInfo, int ttl, long sentAt, boolean highPriority);
}
