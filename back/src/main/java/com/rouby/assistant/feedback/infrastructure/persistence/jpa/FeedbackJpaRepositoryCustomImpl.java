package com.rouby.assistant.feedback.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@RequiredArgsConstructor
public class FeedbackJpaRepositoryCustomImpl implements FeedbackJpaRepositoryCustom{
  
  private final JPAQueryFactory jpaQueryFactory;
}
