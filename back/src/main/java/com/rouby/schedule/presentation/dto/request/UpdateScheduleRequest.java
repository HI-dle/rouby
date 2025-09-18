package com.rouby.schedule.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rouby.schedule.application.dto.command.UpdateScheduleCommand;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateScheduleRequest(
    @NotNull Long parentScheduleId,
    Long targetScheduleId,
    @NotBlank @Size(max = 500) String title,
    @Size(max = 10_000) String memo,
    @PositiveOrZero @Max(10080) Integer alarmOffsetMinutes,
    @NotNull LocalDate overrideDate,
    @NotNull LocalDateTime startAt,
    @NotNull LocalDateTime endAt
) {

  public UpdateScheduleCommand toCommand() {
    return UpdateScheduleCommand.builder()
        .parentScheduleId(parentScheduleId)
        .targetScheduleId(targetScheduleId)
        .title(title)
        .memo(memo)
        .alarmOffsetMinutes(alarmOffsetMinutes)
        .overrideDate(overrideDate)
        .startAt(startAt)
        .endAt(endAt)
        .build();
  }

  @AssertTrue(message = "종료 일자는 시작 일자보다 이후여야 합니다.")
  @JsonIgnore
  public boolean isValidPeriod() {
    return endAt.isAfter(startAt);
  }
}
