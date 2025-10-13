package com.rouby.notification.notificationtemplate.application.service;

import static com.rouby.notification.notificationtemplate.application.exception.NotificationTemplateErrorCode.NOTIFICATION_TEMPLATE_NOT_FOUND;

import com.rouby.notification.notificationtemplate.application.dto.MessageInfo;
import com.rouby.notification.notificationtemplate.application.dto.query.NotificationMessageQuery;
import com.rouby.notification.notificationtemplate.application.exception.NotificationTemplateException;
import com.rouby.notification.notificationtemplate.domain.entity.NotificationTemplate;
import com.rouby.notification.notificationtemplate.domain.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationTemplateReadService {

  private final NotificationTemplateRepository notificationTemplateRepository;

  @Transactional(readOnly = true)
  public MessageInfo generateNotificationMessage(NotificationMessageQuery query) {

    NotificationTemplate template = notificationTemplateRepository.findByType(
            query.notificationType())
        .orElseThrow(() -> NotificationTemplateException.from(NOTIFICATION_TEMPLATE_NOT_FOUND));

    return MessageInfo.of(template.getMessage().getTitle(), template.generateMessageBody(query.username()));
  }
}
