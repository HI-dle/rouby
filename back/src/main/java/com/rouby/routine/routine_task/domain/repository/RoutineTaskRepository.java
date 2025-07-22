package com.rouby.routine.routine_task.domain.repository;

import com.rouby.routine.routine_task.domain.RoutineTask;
import java.util.Optional;

public interface RoutineTaskRepository {
  <S extends RoutineTask> S save(S entity);

  Optional<RoutineTask> findByIdAndDeletedAtIsNull(Long id);
}
