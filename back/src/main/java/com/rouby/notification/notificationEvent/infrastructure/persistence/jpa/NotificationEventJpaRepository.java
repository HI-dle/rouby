package com.rouby.notification.notificationEvent.infrastructure.persistence.jpa;

import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.repository.NotificationEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEventJpaRepository extends
    JpaRepository<NotificationEvent, Long>, NotificationEventRepository,
    NotificationEventJpaRepositoryCustom {

}
