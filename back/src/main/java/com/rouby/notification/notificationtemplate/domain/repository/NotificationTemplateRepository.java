package com.rouby.notification.notificationtemplate.domain.repository;

import com.rouby.notification.notificationtemplate.domain.entity.NotificationTemplate;
import com.rouby.notification.notificationtemplate.domain.entity.NotificationType;
import java.util.Optional;

public interface NotificationTemplateRepository {

  NotificationTemplate save(NotificationTemplate notificationTemplate);

  Optional<NotificationTemplate> findByType(NotificationType type);
}
