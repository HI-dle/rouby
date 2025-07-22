package com.rouby.routine.daily_task.domain.repository;

import com.rouby.routine.daily_task.domain.DailyTask;
import java.time.LocalDate;
import java.util.Optional;

public interface DailyTaskRepository {

  DailyTask save(DailyTask dailyTask);

  Optional<DailyTask> findByIdAndDeletedAtIsNull(Long id);

  boolean existsByRoutineTaskIdAndTaskDate(Long routineTaskId, LocalDate taskDate);
}
