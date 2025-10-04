package com.rouby.schedule.presentation.dto;

import com.rouby.schedule.application.dto.DeleteScheduleCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DeleteScheduleRequest(
    Long scheduleId,
    LocalDate instanceDate,
    LocalDateTime startAt,
    LocalDateTime endAt
) {

  public DeleteScheduleCommand toCommand() {
    return new DeleteScheduleCommand(scheduleId, instanceDate, startAt, endAt);
  }
}
