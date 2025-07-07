package com.rouby.notification.infrastructure.persistence.jpa.emaillog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailLogJpaRepositoryCustomImpl implements EmailLogJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
