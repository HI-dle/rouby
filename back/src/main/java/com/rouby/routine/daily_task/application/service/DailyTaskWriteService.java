package com.rouby.routine.daily_task.application.service;

import static com.rouby.routine.daily_task.application.exception.DailyTaskErrorCode.DAILY_TASK_NOT_FOUND;
import static com.rouby.routine.daily_task.application.exception.DailyTaskErrorCode.DUPLICATE_DAILY_TASK;

import com.rouby.routine.daily_task.application.dto.command.CreateDailyTaskCommand;
import com.rouby.routine.daily_task.application.dto.command.UpdateDailyTaskCommand;
import com.rouby.routine.daily_task.application.exception.DailyTaskException;
import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.daily_task.domain.repository.DailyTaskRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyTaskWriteService {

  private final DailyTaskRepository dailyTaskRepository;

  @Transactional
  public Long createDailyTask(CreateDailyTaskCommand command) {
    ensureNoDuplicateDailyTask(command.routineTaskId(), command.taskDate());
    DailyTask dailyTask = DailyTask.create(
        command.routineTaskId(), command.taskDate(), command.currentValue());

    return dailyTaskRepository.save(dailyTask).getId();
  }

  private void ensureNoDuplicateDailyTask(Long routineTaskId, LocalDate taskDate) {
    if (dailyTaskRepository.existsByRoutineTaskIdAndTaskDate(routineTaskId, taskDate)) {
      throw DailyTaskException.from(DUPLICATE_DAILY_TASK);
    }
  }

  @Transactional
  public Long updateDailyTask(UpdateDailyTaskCommand command) {
    DailyTask dailyTask = dailyTaskRepository.findByIdAndDeletedAtIsNull(command.id())
        .orElseThrow(() -> DailyTaskException.from(DAILY_TASK_NOT_FOUND));

    dailyTask.update(command.routineTaskId(), command.taskDate(), command.currentValue());
    return dailyTask.getId();
  }
}
