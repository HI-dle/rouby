package com.rouby.notification.domain.repository.emaillog;

import com.rouby.notification.domain.entity.emaillog.EmailLog;

public interface EmailLogRepository {

  EmailLog save(EmailLog entity);
}
