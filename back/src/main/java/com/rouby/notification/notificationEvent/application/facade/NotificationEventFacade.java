package com.rouby.notification.notificationEvent.application.facade;

import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventCommand;
import com.rouby.notification.notificationEvent.application.service.NotificationEventWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventFacade {

  private final NotificationEventWriteService notificationEventWriteService;

  public void sendNotification(CreateNotificationEventCommand command) {
    notificationEventWriteService.sendNotification(command);
  }

  public void create(CreateNotificationEventCommand command) {
    notificationEventWriteService.create(command);
  }
}
