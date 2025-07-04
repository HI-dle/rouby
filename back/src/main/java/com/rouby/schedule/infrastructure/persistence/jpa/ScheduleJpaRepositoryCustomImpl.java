package com.rouby.schedule.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleJpaRepositoryCustomImpl implements ScheduleJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

}
