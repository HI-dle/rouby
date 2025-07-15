package com.rouby.routine.routine_task.application.service;

import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineTaskWriteService {
  private final RoutineTaskRepository routineTaskRepository;


  public Long createRoutineTask(CreateRoutineTaskCommand command) {
    RoutineTask save = routineTaskRepository.save(command.toEntity());
    return save.getId();
  }
}
