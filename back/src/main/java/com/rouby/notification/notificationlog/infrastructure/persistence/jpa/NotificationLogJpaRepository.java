package com.rouby.notification.notificationlog.infrastructure.persistence.jpa;

import com.rouby.notification.notificationlog.domain.entity.NotificationLog;
import com.rouby.notification.notificationlog.domain.repository.NotificationLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogJpaRepository extends
    JpaRepository<NotificationLog, Long>, NotificationLogRepository,
    NotificationLogJpaRepositoryCustom {

}
