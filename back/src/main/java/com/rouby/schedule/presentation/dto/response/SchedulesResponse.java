package com.rouby.schedule.presentation.dto.response;

import com.rouby.schedule.application.dto.info.SchedulesInfo;
import com.rouby.schedule.application.dto.info.SchedulesInfo.RecurrenceRuleInfo;
import com.rouby.schedule.application.dto.info.SchedulesInfo.ScheduleInfo;
import com.rouby.schedule.application.dto.info.SchedulesInfo.ScheduleOverrideInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record SchedulesResponse(
    List<ScheduleResponse> schedules
) {

  public static SchedulesResponse of(SchedulesInfo infos) {
    return SchedulesResponse.builder()
        .schedules(infos.schedules().stream()
            .map(SchedulesResponse::mapToScheduleResponse
            )
            .toList())
        .build();
  }

  private static ScheduleResponse mapToScheduleResponse(ScheduleInfo info) {
    return ScheduleResponse.builder()
        .id(info.id())
        .userId(info.userId())
        .title(info.title())
        .memo(info.memo())
        .alarmOffsetMinutes(info.alarmOffsetMinutes())
        .startAt(info.startAt())
        .endAt(info.endAt())
        .routineOffsetDays(info.routineOffsetDays())
        .recurrenceRule(
            mapToRRuleResponse(info.recurrenceRule())
        )
        .scheduleOverrides(
            info.scheduleOverrides().stream()
                .map(SchedulesResponse::mapToOverrideResponse
                )
                .toList()
        )
        .build();
  }

  private static RecurrenceRuleResponse mapToRRuleResponse(RecurrenceRuleInfo rRuleInfo) {
    if  (rRuleInfo == null) return null;

    return RecurrenceRuleResponse.builder()
        .freq(rRuleInfo.freq())
        .until(rRuleInfo.until())
        .interval(rRuleInfo.interval())
        .byDay(rRuleInfo.byDay())
        .rruleStr(rRuleInfo.rruleStr())
        .build();
  }

  private static ScheduleOverrideResponse mapToOverrideResponse(ScheduleOverrideInfo override) {
    return ScheduleOverrideResponse.builder()
        .id(override.id())
        .userId(override.userId())
        .startAt(override.startAt())
        .endAt(override.endAt())
        .title(override.title())
        .memo(override.memo())
        .alarmOffsetMinutes(override.alarmOffsetMinutes())
        .routineOffsetDays(override.routineOffsetDays())
        .overrideDate(override.overrideDate())
        .overrideType(override.overrideType())
        .build();
  }


  @Builder
  public record ScheduleResponse(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      Integer routineOffsetDays,
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
      LocalDateTime until,
      String rruleStr
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
      Integer routineOffsetDays,
      Integer alarmOffsetMinutes,
      String overrideType,
      LocalDate overrideDate
  ) {
  }
}
