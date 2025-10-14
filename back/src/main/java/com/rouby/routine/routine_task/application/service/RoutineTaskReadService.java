package com.rouby.routine.routine_task.application.service;

import static com.rouby.routine.routine_task.application.exception.RoutineTaskErrorCode.ROUTINE_TASK_ACCESS_DENIED;
import static com.rouby.routine.routine_task.application.exception.RoutineTaskErrorCode.ROUTINE_TASK_NOT_FOUND;

import com.rouby.common.utils.JsonHelper;
import com.rouby.routine.routine_task.application.dto.command.GetRoutineTaskCommand;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import com.rouby.routine.routine_task.application.dto.info.RoutineTaskSummaryInfo;
import com.rouby.routine.routine_task.application.exception.RoutineTaskException;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineTaskReadService {

  private final RoutineTaskRepository routineTaskRepository;
  private final JsonHelper jsonHelper;

  @Transactional(readOnly = true)
  public void ensureRoutineTaskOwner(Long routineTaskId, Long userId) {
    RoutineTask task = routineTaskRepository.findByIdAndDeletedAtIsNull(routineTaskId)
        .orElseThrow(() -> RoutineTaskException.from(ROUTINE_TASK_NOT_FOUND));

    if (!task.isOwner(userId)) {
      throw RoutineTaskException.from(ROUTINE_TASK_ACCESS_DENIED);
    }
  }

  @Transactional(readOnly = true)
  public GetRoutineTaskInfo getRoutineTask(GetRoutineTaskCommand command) {
    return GetRoutineTaskInfo.of(
        routineTaskRepository.findRoutineTaskByCriteria(command.toCriteria()));
  }

  @Transactional(readOnly = true)
  public List<RoutineTaskWithOverrides> getRoutineTasksWithOverrides(
      GetRoutineTaskCommand command) {
    return routineTaskRepository.findRoutineTaskByCriteria(command.toCriteria());
  }

  public String findSummaryRoutineTaskJsonBy(GetRoutineTaskCommand command) {
    return jsonHelper.toPrettyJson(RoutineTaskSummaryInfo.of(
        routineTaskRepository.findRoutineTaskByCriteria(command.toCriteria())));
  }
}
