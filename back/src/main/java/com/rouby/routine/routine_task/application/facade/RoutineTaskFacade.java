package com.rouby.routine.routine_task.application.facade;

import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand;
import com.rouby.routine.routine_task.application.dto.command.GetRoutineTaskCommand;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import com.rouby.routine.routine_task.application.service.RoutineTaskReadService;
import com.rouby.routine.routine_task.application.service.RoutineTaskWriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoutineTaskFacade {

  private final RoutineTaskWriteService routineTaskWriteService;
  private final RoutineTaskReadService routineTaskReadService;

  public Long createRoutineTask(CreateRoutineTaskCommand command) {
    return routineTaskWriteService.createRoutineTask(command);
  }

  public GetRoutineTaskInfo getRoutineTask(GetRoutineTaskCommand command) {
    return routineTaskReadService.getRoutineTask(command);
  }
}
