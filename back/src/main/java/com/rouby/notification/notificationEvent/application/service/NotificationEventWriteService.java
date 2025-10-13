package com.rouby.notification.notificationEvent.application.service;

import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventWithTemplateCommand;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationEventWriteService {

  private final NotificationEventRepository notificationEventRepository;

  @Transactional
  public void create(CreateNotificationEventWithTemplateCommand command) {

    notificationEventRepository.save(command.toEntity(Boolean.TRUE));
  }

  @Transactional
  public void createEvents(List<CreateNotificationEventWithTemplateCommand> commands,
      Boolean notificationEnabled) {

    notificationEventRepository.saveAll(commands.stream()
        .map(command -> command.toEntity(notificationEnabled))
        .toList());
  }
}
