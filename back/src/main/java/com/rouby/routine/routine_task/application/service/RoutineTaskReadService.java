package com.rouby.routine.routine_task.application.service;

import static com.rouby.routine.routine_task.application.exception.RoutineTaskErrorCode.ROUTINE_TASK_ACCESS_DENIED;
import static com.rouby.routine.routine_task.application.exception.RoutineTaskErrorCode.ROUTINE_TASK_NOT_FOUND;

import com.rouby.routine.routine_task.application.exception.RoutineTaskException;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineTaskReadService {

  private final RoutineTaskRepository routineTaskRepository;

  @Transactional(readOnly = true)
  public void ensureRoutineTaskOwner(Long routineTaskId, Long userId) {
    RoutineTask task = routineTaskRepository.findByIdAndDeletedAtIsNull(routineTaskId)
        .orElseThrow(() -> RoutineTaskException.from(ROUTINE_TASK_NOT_FOUND));

    if (!task.isOwner(userId)) {
      throw RoutineTaskException.from(ROUTINE_TASK_ACCESS_DENIED);
    }
  }
}
