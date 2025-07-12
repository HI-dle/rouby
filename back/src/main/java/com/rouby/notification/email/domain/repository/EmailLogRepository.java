package com.rouby.notification.email.domain.repository;

import com.rouby.notification.email.domain.entity.EmailLog;

public interface EmailLogRepository {

  EmailLog save(EmailLog entity);
}
