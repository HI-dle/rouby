package com.rouby.notification.email.infrastructure.persistence.jpa;

import com.rouby.notification.email.domain.entity.EmailLog;
import com.rouby.notification.email.domain.repository.EmailLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogJpaRepository extends
    JpaRepository<EmailLog, Long>, EmailLogRepository {
}
