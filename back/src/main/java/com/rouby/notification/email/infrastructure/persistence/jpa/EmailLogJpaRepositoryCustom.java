package com.rouby.notification.email.infrastructure.persistence.jpa;

import com.rouby.notification.email.domain.entity.EmailType;

public interface EmailLogJpaRepositoryCustom {

  long countSentEmailIsToday(String email, EmailType emailType);

}
