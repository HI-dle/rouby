package com.rouby.notification.notificationtemplate.application.service;

import com.rouby.notification.notificationtemplate.application.dto.command.CreateNotificationTemplateCommand;
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
    notificationTemplateRepository.save(command.toEntity());
  }
}
