package com.rouby.notification.notificationlog.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationLogJpaRepositoryCustomImpl implements NotificationLogJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
