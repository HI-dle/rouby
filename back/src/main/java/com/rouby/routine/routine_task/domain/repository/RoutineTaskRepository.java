package com.rouby.routine.routine_task.domain.repository;

import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.search.GetRoutineTaskCriteria;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import java.util.List;
import java.util.Optional;

public interface RoutineTaskRepository {
  <S extends RoutineTask> S save(S entity);

  Optional<RoutineTask> findByIdAndDeletedAtIsNull(Long id);

  List<RoutineTaskWithOverrides> findRoutineTaskByCriteria(GetRoutineTaskCriteria getRoutineTaskCriteria);
}
