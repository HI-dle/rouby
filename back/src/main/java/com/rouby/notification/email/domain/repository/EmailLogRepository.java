package com.rouby.notification.email.domain.repository;

import com.rouby.notification.email.domain.entity.EmailLog;
import com.rouby.notification.email.domain.entity.EmailType;

public interface EmailLogRepository {

  EmailLog save(EmailLog entity);

  long countSentEmailIsToday(String email, EmailType emailType);
}
