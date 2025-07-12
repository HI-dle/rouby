package com.rouby.notification.notificationtemplate.infrastructure.persistence.jpa;

import com.rouby.notification.notificationtemplate.domain.entity.NotificationTemplate;
import com.rouby.notification.notificationtemplate.domain.repository.NotificationTemplateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateJpaRepository extends
    JpaRepository<NotificationTemplate, Long>, NotificationTemplateRepository,
    NotificationTemplateJpaRepositoryCustom {

}
