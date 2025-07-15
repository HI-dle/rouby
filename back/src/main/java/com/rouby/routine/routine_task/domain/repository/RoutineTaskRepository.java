package com.rouby.routine.routine_task.domain.repository;

import com.rouby.routine.routine_task.domain.RoutineTask;

public interface RoutineTaskRepository {
  <S extends RoutineTask> S save(S entity);
}
