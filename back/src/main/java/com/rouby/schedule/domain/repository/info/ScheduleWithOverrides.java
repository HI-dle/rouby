package com.rouby.schedule.domain.repository.info;

import com.rouby.schedule.domain.enums.AlarmOffsetType;
import com.rouby.schedule.domain.enums.OverrideType;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleWithOverrides(
    Long id,
    Long userId,
    String title,
    String memo,
    LocalDateTime startAt,
    LocalDateTime endAt,
    Integer routineOffsetDays,
    AlarmOffsetType alarmOffsetType,
    RecurrenceRule recurrenceRule,
    List<ScheduleOverride> scheduleOverrides
) {

  @Builder
  public record ScheduleOverride(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      Integer routineOffsetDays,
      AlarmOffsetType alarmOffsetType,
      OverrideType overrideType,
      LocalDate overrideDate
  ) {
  }
}
