package com.rouby.routine.routine_task.infrastructure.persistence.jpa.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskResult;
import com.rouby.routine.routine_task.infrastructure.persistence.jpa.RoutineTaskJpaRepositoryCustom;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 31.
 */
@Repository
@RequiredArgsConstructor
public class RoutineTaskJpaRepositoryCustomImpl implements RoutineTaskJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public List<RoutineTaskResult> findOneMonthByUserId(Long userId, LocalDate startDate,
      LocalDate endDate) {


    return null;
  }
}
