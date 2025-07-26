package com.rouby.routine.daily_task.presentation.dto;

import com.rouby.routine.daily_task.application.dto.command.ProgressDailyTaskCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record ProgressDailyTaskRequest(
    @Positive Long id,
    @Positive @NotNull Long routineTaskId,
    @NotNull @PositiveOrZero Integer currentValue,
    @NotNull LocalDate taskDate
) {
  public ProgressDailyTaskCommand toCommand(Long userId) {
    return ProgressDailyTaskCommand.builder()
        .id(id)
        .userId(userId)
        .routineTaskId(routineTaskId)
        .currentValue(currentValue)
        .taskDate(taskDate)
        .build();
  }
}
