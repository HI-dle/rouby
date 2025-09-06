package com.rouby.notification.notificationtemplate.application.facade;

import com.rouby.notification.notificationtemplate.application.dto.query.NotificationMessageQuery;
import com.rouby.notification.notificationtemplate.application.service.NotificationTemplateReadService;
import com.rouby.notification.notificationtemplate.domain.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTemplateFacade {

  private final NotificationTemplateReadService notificationTemplateReadService;

  public Message generateNotificationMessage(NotificationMessageQuery query) {
    return notificationTemplateReadService.generateNotificationMessage(query);
  }

}
