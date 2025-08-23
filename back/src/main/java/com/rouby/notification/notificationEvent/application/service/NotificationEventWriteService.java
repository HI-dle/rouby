package com.rouby.notification.notificationEvent.application.service;

import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventCommand;
import com.rouby.notification.notificationEvent.domain.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationEventWriteService {
  private final NotificationSender notificationSender;

  public void sendNotification(CreateNotificationEventCommand command) {
    this.notificationSender.send(command.toEntity());
  }
}
