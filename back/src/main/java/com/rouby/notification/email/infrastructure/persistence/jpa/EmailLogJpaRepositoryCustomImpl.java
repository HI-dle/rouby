package com.rouby.notification.email.infrastructure.persistence.jpa;

import static com.rouby.notification.email.domain.entity.SendStatus.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.notification.email.domain.entity.EmailAddress;
import com.rouby.notification.email.domain.entity.EmailType;
import com.rouby.notification.email.domain.entity.QEmailLog;
import com.rouby.notification.email.domain.entity.SendStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailLogJpaRepositoryCustomImpl implements EmailLogJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public long countSentEmailIsToday(String email, EmailType emailType) {
    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    LocalDateTime endOfDay = startOfDay.plusDays(1);

    QEmailLog emailLog = QEmailLog.emailLog;

    return jpaQueryFactory
        .select(emailLog.count())
        .from(emailLog)
        .where(
            emailLog.address.emilAddress.eq(email),
            emailLog.type.eq(emailType),
            emailLog.status.eq(SENT),
            emailLog.createdAt.goe(startOfDay),
            emailLog.createdAt.lt(endOfDay)
        )
        .fetchOne();
  }
}
