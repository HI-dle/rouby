package com.rouby.notification.infrastructure.persistence.jpa.emaillog;

import com.rouby.notification.domain.entity.emaillog.EmailLog;
import com.rouby.notification.domain.repository.emaillog.EmailLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogJpaRepository extends
    JpaRepository<EmailLog, Long>, EmailLogRepository,
    EmailLogJpaRepositoryCustom {

}
