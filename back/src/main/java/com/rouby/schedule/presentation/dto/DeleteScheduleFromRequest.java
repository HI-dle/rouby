package com.rouby.schedule.presentation.dto;

import com.rouby.schedule.application.dto.DeleteScheduleFromCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DeleteScheduleFromRequest(

    @NotNull(message = "스케줄 ID는 필수입니다.")
    Long scheduleId,

    @NotNull(message = "삭제 기준 시점은 필수입니다.")
    LocalDateTime fromAt
) {

  public DeleteScheduleFromCommand toCommand() {
    return new DeleteScheduleFromCommand(scheduleId, fromAt);
  }
}
