package com.rouby.notification.email.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailLogJpaRepositoryCustomImpl {

  private final JPAQueryFactory jpaQueryFactory;

}
