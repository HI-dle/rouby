package com.rouby.assistant.briefing.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@RequiredArgsConstructor
public class BriefingJpaRepositoryCustomImpl implements BriefingJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
}
