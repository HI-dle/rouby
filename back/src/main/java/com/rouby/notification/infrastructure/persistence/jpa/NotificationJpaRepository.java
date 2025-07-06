package com.rouby.notification.infrastructure.persistence.jpa;

import com.rouby.notification.domain.entity.Notification;
import com.rouby.notification.domain.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends
    JpaRepository<Notification, Long>, NotificationRepository, NotificationJpaRepositoryCustom {

}
