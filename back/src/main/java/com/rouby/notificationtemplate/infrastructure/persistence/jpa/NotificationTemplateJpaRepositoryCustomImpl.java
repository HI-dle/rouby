package com.rouby.notificationtemplate.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationTemplateJpaRepositoryCustomImpl implements
    NotificationTemplateJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
