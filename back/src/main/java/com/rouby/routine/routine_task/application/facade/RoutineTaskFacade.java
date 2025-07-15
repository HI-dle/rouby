package com.rouby.routine.routine_task.application.facade;

import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand;
import com.rouby.routine.routine_task.application.service.RoutineTaskWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoutineTaskFacade {

  private final RoutineTaskWriteService routineTaskWriteService;

  public Long createRoutineTask(CreateRoutineTaskCommand command) {
    return routineTaskWriteService.createRoutineTask(command);
  }
}
