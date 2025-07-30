package com.rouby.routine.daily_task.application.facade;

import com.rouby.routine.daily_task.application.dto.command.ProgressDailyTaskCommand;
import com.rouby.routine.daily_task.application.service.DailyTaskReadService;
import com.rouby.routine.daily_task.application.service.DailyTaskWriteService;
import com.rouby.routine.routine_task.application.service.RoutineTaskReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyTaskFacade {

  private final DailyTaskReadService dailyTaskReadService;
  private final DailyTaskWriteService dailyTaskWriteService;
  private final RoutineTaskReadService routineTaskReadService;

  public Long progressDailyTask(ProgressDailyTaskCommand command) {
    routineTaskReadService.ensureRoutineTaskOwner(command.routineTaskId(), command.userId());

    if (command.id() == null) {
      return dailyTaskWriteService.createDailyTask(command.toCreate());
    } else {
      return dailyTaskWriteService.updateDailyTask(command.toUpdate());
    }
  }
}
