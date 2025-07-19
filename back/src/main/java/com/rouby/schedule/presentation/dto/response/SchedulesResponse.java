package com.rouby.schedule.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record SchedulesResponse(
    List<ScheduleResponse> schedules
) {

  @Builder
  public record ScheduleResponse(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      LocalDate routineActivateDate,
      Integer alarmOffsetMinutes,
      RecurrenceRuleResponse recurrenceRule,
      List<ScheduleOverrideResponse> scheduleOverrides
  ) {
  }

  @Builder
  public record RecurrenceRuleResponse(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDateTime until
  ) {
  }

  @Builder
  public record ScheduleOverrideResponse(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      Integer alarmOffsetMinutes,
      String overrideType,
      LocalDate overrideDate
  ) {
  }
}
