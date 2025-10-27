package com.rouby.assistant.feedback.infrastructure.adapter;

import com.rouby.assistant.feedback.application.dto.RoutineTasksInfoForFeedback;
import com.rouby.assistant.feedback.application.port.outbound.RoutineTaskGateway;
import com.rouby.routine.routine_task.application.dto.command.GetRoutineTaskCommand;
import com.rouby.routine.routine_task.application.usecase.RoutineTaskUsecase;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoutineTaskGatewayAdapter implements RoutineTaskGateway {

  private final RoutineTaskUsecase routineTaskUsecase;

  @Override
  public RoutineTasksInfoForFeedback getRoutineTaskWithProgress(
      Long userId, LocalDate fromDate, LocalDate toDate) {

    return RoutineTasksInfoForFeedback.from(
        routineTaskUsecase.getRoutineTaskWithProgress(
            GetRoutineTaskCommand.builder()
                .userId(userId)
                .fromDate(fromDate)
                .toDate(toDate)
                .build()));
  }
}
