package com.rouby.assistant.feedback.application.dto;

import com.rouby.schedule.application.dto.info.SchedulesInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record SchedulesInfoForFeedback(
    List<ScheduleInfo> schedules
) {

  public static SchedulesInfoForFeedback from(SchedulesInfo schedulesInfo) {
    return SchedulesInfoForFeedback.builder()
        .schedules(schedulesInfo.schedules() == null
            ? List.of()
            : schedulesInfo.schedules().stream().map(ScheduleInfo::from).toList())
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

    public static ScheduleInfo from(SchedulesInfo.ScheduleInfo scheduleInfo) {
      return ScheduleInfo.builder()
          .id(scheduleInfo.id())
          .userId(scheduleInfo.userId())
          .title(scheduleInfo.title())
          .memo(scheduleInfo.memo())
          .startAt(scheduleInfo.startAt())
          .endAt(scheduleInfo.endAt())
          .routineOffsetDays(scheduleInfo.routineOffsetDays())
          .alarmOffsetMinutes(scheduleInfo.alarmOffsetMinutes())
          .recurrenceRule(
              scheduleInfo.recurrenceRule() == null
                  ? null
                  : RecurrenceRuleInfo.from(scheduleInfo.recurrenceRule())
          )
          .scheduleOverrides(
              scheduleInfo.scheduleOverrides() == null
                  ? List.of()
                  : scheduleInfo.scheduleOverrides().stream()
                      .map(ScheduleOverrideInfo::from).toList()
          )
          .build();
    }
  }

  @Builder
  public record RecurrenceRuleInfo(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDateTime until,
      String rruleStr
  ) {

    public static RecurrenceRuleInfo from(SchedulesInfo.RecurrenceRuleInfo recurrenceRuleInfo) {

      if (recurrenceRuleInfo == null) return null;

      return RecurrenceRuleInfo.builder()
          .freq(recurrenceRuleInfo.freq())
          .byDay(recurrenceRuleInfo.byDay())
          .interval(recurrenceRuleInfo.interval())
          .until(recurrenceRuleInfo.until())
          .rruleStr(recurrenceRuleInfo.rruleStr())
          .build();
    }
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

    public static ScheduleOverrideInfo from(SchedulesInfo.ScheduleOverrideInfo scheduleOverrideInfo) {

      if (scheduleOverrideInfo == null) return null;

      return ScheduleOverrideInfo.builder()
          .id(scheduleOverrideInfo.id())
          .userId(scheduleOverrideInfo.userId())
          .title(scheduleOverrideInfo.title())
          .memo(scheduleOverrideInfo.memo())
          .startAt(scheduleOverrideInfo.startAt())
          .endAt(scheduleOverrideInfo.endAt())
          .routineOffsetDays(scheduleOverrideInfo.routineOffsetDays())
          .alarmOffsetMinutes(scheduleOverrideInfo.alarmOffsetMinutes())
          .overrideType(scheduleOverrideInfo.overrideType())
          .overrideDate(scheduleOverrideInfo.overrideDate())
          .build();
    }
  }
}
