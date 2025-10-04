package com.rouby.schedule.presentation.dto;

import com.rouby.schedule.application.dto.DeleteScheduleFromCommand;
import java.time.LocalDateTime;

public record DeleteScheduleFromRequest(
    Long scheduleId,
    LocalDateTime fromAt
) {

  public DeleteScheduleFromCommand toCommand() {
    return new DeleteScheduleFromCommand(scheduleId, fromAt);
  }
}
