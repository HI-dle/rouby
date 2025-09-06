package com.rouby.notification.notificationtemplate.application.service;

import static com.rouby.notification.notificationtemplate.application.exception.NotificationTemplateErrorCode.NOTIFICATION_TEMPLATE_NOT_FOUND;

import com.rouby.notification.notificationtemplate.application.dto.query.NotificationMessageQuery;
import com.rouby.notification.notificationtemplate.application.exception.NotificationTemplateException;
import com.rouby.notification.notificationtemplate.domain.entity.Message;
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
  public Message generateNotificationMessage(NotificationMessageQuery query) {
    NotificationTemplate template = notificationTemplateRepository.findByType(
            query.notificationType())
        .orElseThrow(() -> NotificationTemplateException.from(NOTIFICATION_TEMPLATE_NOT_FOUND));
    String body = template.generateMessageBody(query.username());
    return Message.of(template.getMessage().getTitle(), body);
  }

}
