package com.rouby.notification.infrastructure.persistence.jpa.notificationlog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationLogJpaRepositoryCustomImpl implements NotificationLogJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
