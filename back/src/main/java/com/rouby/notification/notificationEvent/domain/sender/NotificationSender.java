package com.rouby.notification.notificationEvent.domain.sender;

import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;

public interface NotificationSender {

  void send(NotificationEvent event);
}
