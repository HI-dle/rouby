package com.rouby.schedule.presentation.dto;

import com.rouby.schedule.application.dto.DeleteScheduleCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DeleteScheduleRequest(

    @NotNull(message = "스케줄 ID는 필수입니다.")
    Long scheduleId,

    @NotNull(message = "인스턴스 날짜는 필수입니다.")
    LocalDate instanceDate,

    @NotNull(message = "시작일자는 필수입니다.")
    LocalDateTime startAt,

    @NotNull(message = "종료일자는 필수입니다.")
    LocalDateTime endAt
) {

  public DeleteScheduleCommand toCommand() {
    return new DeleteScheduleCommand(scheduleId, instanceDate, startAt, endAt);
  }
}
