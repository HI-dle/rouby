package com.rouby.notificationtemplate.infrastructure.persistence.jpa;

import com.rouby.notificationtemplate.domain.entity.NotificationTemplate;
import com.rouby.notificationtemplate.domain.repository.NotificationTemplateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateJpaRepository extends
    JpaRepository<NotificationTemplate, Long>, NotificationTemplateRepository,
    NotificationTemplateJpaRepositoryCustom {

}
