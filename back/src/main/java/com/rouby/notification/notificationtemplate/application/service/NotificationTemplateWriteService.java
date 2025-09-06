package com.rouby.notification.notificationtemplate.application.service;

import static com.rouby.notification.notificationtemplate.application.exception.NotificationTemplateErrorCode.NOTIFICATION_TEMPLATE_ALREADY_EXISTS;

import com.rouby.notification.notificationtemplate.application.dto.command.CreateNotificationTemplateCommand;
import com.rouby.notification.notificationtemplate.application.exception.NotificationTemplateException;
import com.rouby.notification.notificationtemplate.domain.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationTemplateWriteService {

  private final NotificationTemplateRepository notificationTemplateRepository;

  @Transactional
  public void create(CreateNotificationTemplateCommand command) {
    if (notificationTemplateRepository.existsByType(command.notificationType())) {
      throw NotificationTemplateException.from(NOTIFICATION_TEMPLATE_ALREADY_EXISTS);
    }
    notificationTemplateRepository.save(command.toEntity());
  }
}
