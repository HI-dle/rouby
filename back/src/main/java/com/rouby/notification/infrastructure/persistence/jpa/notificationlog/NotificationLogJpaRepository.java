package com.rouby.notification.infrastructure.persistence.jpa.notificationlog;

import com.rouby.notification.domain.entity.notificationlog.NotificationLog;
import com.rouby.notification.domain.repository.notificationlog.NotificationLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogJpaRepository extends
    JpaRepository<NotificationLog, Long>, NotificationLogRepository,
    NotificationLogJpaRepositoryCustom {

}
