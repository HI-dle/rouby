package com.rouby.notification.notificationEvent.application.service;

import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventCommand;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import com.rouby.notification.notificationEvent.domain.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationEventWriteService {

  private final NotificationSender notificationSender;
  private final NotificationEventRepository notificationEventRepository;

  public void sendNotification(CreateNotificationEventCommand command) {
    this.notificationSender.send(command.toEntity());
  }

  @Transactional
  public void create(CreateNotificationEventCommand command) {
    notificationEventRepository.save(command.toEntity());
  }
}
