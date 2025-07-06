package com.rouby.notification.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationJpaRepositoryCustomImpl implements NotificationJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
