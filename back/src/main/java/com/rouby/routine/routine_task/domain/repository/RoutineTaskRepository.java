package com.rouby.routine.routine_task.domain.repository;

import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskResult;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineTaskRepository {
  <S extends RoutineTask> S save(S entity);

  Optional<RoutineTask> findByIdAndDeletedAtIsNull(Long id);

  List<RoutineTaskResult> findOneMonthByUserId(Long userId, LocalDate startDate, LocalDate endDate);
}
