package com.rouby.routine.daily_task.application.dto.command;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ProgressDailyTaskCommand(
    Long id,
    Long userId,
    Long routineTaskId,
    Integer currentValue,
    LocalDate taskDate
) {
  public CreateDailyTaskCommand toCreate(){
    return CreateDailyTaskCommand.builder()
        .routineTaskId(routineTaskId)
        .currentValue(currentValue)
        .taskDate(taskDate)
        .build();
  }

  public UpdateDailyTaskCommand toUpdate(){
    return UpdateDailyTaskCommand.builder()
        .id(id)
        .routineTaskId(routineTaskId)
        .currentValue(currentValue)
        .taskDate(taskDate)
        .build();
  }
}
