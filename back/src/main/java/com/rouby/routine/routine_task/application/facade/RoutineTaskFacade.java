package com.rouby.routine.routine_task.application.facade;

import com.rouby.routine.daily_task.application.service.DailyTaskReadService;
import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand;
import com.rouby.routine.routine_task.application.dto.command.GetRoutineTaskCommand;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import com.rouby.routine.routine_task.application.service.RoutineTaskReadService;
import com.rouby.routine.routine_task.application.service.RoutineTaskWriteService;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoutineTaskFacade {

  private final RoutineTaskWriteService routineTaskWriteService;
  private final RoutineTaskReadService routineTaskReadService;
  private final DailyTaskReadService dailyTaskReadService;

  public Long createRoutineTask(CreateRoutineTaskCommand command) {
    return routineTaskWriteService.createRoutineTask(command);
  }

  public GetRoutineTaskInfo getRoutineTask(GetRoutineTaskCommand command) {
    return routineTaskReadService.getRoutineTask(command);
  }

  public GetRoutineTaskInfo getRoutineTaskWithProgress(GetRoutineTaskCommand command) {
    List<RoutineTaskWithOverrides> routines = routineTaskReadService
        .getRoutineTasksWithOverrides(command);

    List<Long> routineIds = routines.stream()
        .map(RoutineTaskWithOverrides::id)
        .toList();

    List<DailyTask> dailyTasks = dailyTaskReadService
        .getDailyTasks(routineIds, command.fromDate(), command.toDate());

    return GetRoutineTaskInfo.ofWithProgress(routines, dailyTasks);
  }
}
