package com.rouby.assistant.recommendation.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@RequiredArgsConstructor
public class RecommendationJpaRepositoryCustomImpl implements RecommendationJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
}
