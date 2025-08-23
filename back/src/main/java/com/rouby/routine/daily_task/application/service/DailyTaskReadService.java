package com.rouby.routine.daily_task.application.service;

import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.daily_task.domain.repository.DailyTaskRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyTaskReadService {

  private final DailyTaskRepository dailyTaskRepository;

  @Transactional(readOnly = true)
  public List<DailyTask> getDailyTasks(
      List<Long> routineTaskIds, LocalDate fromDate, LocalDate toDate) {

    return dailyTaskRepository.findByRoutineTaskIdInAndTaskDateBetween(
        routineTaskIds, fromDate, toDate);
  }
}
