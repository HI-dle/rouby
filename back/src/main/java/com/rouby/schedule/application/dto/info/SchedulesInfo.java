package com.rouby.schedule.application.dto.info;

import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides.ScheduleOverride;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record SchedulesInfo(
    List<ScheduleInfo> schedules
) {

  public static SchedulesInfo of(List<ScheduleWithOverrides> schedulesByCriteria) {

    return SchedulesInfo.builder()
        .schedules(schedulesByCriteria.stream()
            .map(SchedulesInfo::mapToScheduleInfo
            )
            .toList())
        .build();
  }

  private static ScheduleInfo mapToScheduleInfo(ScheduleWithOverrides schedule) {
    return ScheduleInfo.builder()
        .id(schedule.id())
        .userId(schedule.userId())
        .title(schedule.title())
        .memo(schedule.memo())
        .startAt(schedule.startAt())
        .endAt(schedule.endAt())
        .routineOffsetDays(schedule.routineOffsetDays())
        .alarmOffsetMinutes(schedule.alarmOffsetType() == null ? null : schedule.alarmOffsetType().getMinutes())
        .recurrenceRule(mapToRRuleInfo(schedule.recurrenceRule()))
        .scheduleOverrides(
            schedule.scheduleOverrides().stream()
                .map(SchedulesInfo::mapToScheduleOverrideInfo
                )
                .toList()
        )
        .build();
  }

  private static RecurrenceRuleInfo mapToRRuleInfo(RecurrenceRule rrule) {
    if (rrule == null) return null;

    return RecurrenceRuleInfo.builder()
        .freq(rrule.getFreq() == null ? null : rrule.getFreq().toString())
        .interval(rrule.getInterval())
        .until(rrule.getUntil())
        .byDay(rrule.getByDay() == null
            ? null
            : rrule.getByDay().stream()
            .map(Enum::toString)
            .collect(Collectors.toSet()))
        .rruleStr(rrule.toRruleString())
        .build();
  }

  private static ScheduleOverrideInfo mapToScheduleOverrideInfo(ScheduleOverride override) {
    return ScheduleOverrideInfo.builder()
        .id(override.id())
        .userId(override.userId())
        .startAt(override.startAt())
        .endAt(override.endAt())
        .title(override.title())
        .memo(override.memo())
        .routineOffsetDays(override.routineOffsetDays())
        .alarmOffsetMinutes(override.alarmOffsetType() == null ? null : override.alarmOffsetType().getMinutes())
        .overrideDate(override.overrideDate())
        .overrideType(override.overrideType().toString())
        .build();
  }


  @Builder
  public record ScheduleInfo(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      Integer routineOffsetDays,
      Integer alarmOffsetMinutes,
      RecurrenceRuleInfo recurrenceRule,
      List<ScheduleOverrideInfo> scheduleOverrides
  ) {
  }

  @Builder
  public record RecurrenceRuleInfo(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDateTime until,
      String rruleStr
  ) {
  }

  @Builder
  public record ScheduleOverrideInfo(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      Integer routineOffsetDays,
      Integer alarmOffsetMinutes,
      String overrideType,
      LocalDate overrideDate
  ) {
  }
}
