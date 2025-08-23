package com.rouby.notification.notificationEvent.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationEventJpaRepositoryCustomImpl implements
    NotificationEventJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
