package com.rouby.routine.routine_task.infrastructure.persistence.jpa;

import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskResult;
import java.time.LocalDate;
import java.util.List;

public interface RoutineTaskJpaRepositoryCustom {

  List<RoutineTaskResult> findOneMonthByUserId(Long userId, LocalDate startDate, LocalDate endDate);

}
