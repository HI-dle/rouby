package com.rouby.routine.daily_task.application.dto.command;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UpdateDailyTaskCommand(
    Long id,
    Long routineTaskId,
    Integer currentValue,
    LocalDate taskDate
) {

}
