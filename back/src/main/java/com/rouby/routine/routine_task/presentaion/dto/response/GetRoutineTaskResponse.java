package com.rouby.routine.routine_task.presentaion.dto.response;

import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import lombok.Builder;

@Builder
public record GetRoutineTaskResponse(
    List<RoutineTaskResponse> routineTasks
) {

  public static GetRoutineTaskResponse of(GetRoutineTaskInfo info) {
    return GetRoutineTaskResponse.builder()
        .routineTasks(info.routines().stream()
            .map(GetRoutineTaskResponse::mapToResponse)
            .toList())
        .build();
  }

  private static RoutineTaskResponse mapToResponse(RoutineTask task) {
    return RoutineTaskResponse.builder()
        .id(task.id())
        .userId(task.userId())
        .title(task.title())
        .taskType(task.taskType())
        .alarmOffsetMinutes(task.alarmOffsetMinutes())
        .routineTimeInfo(mapToRoutineTimeInfo(task.routineTimeInfo()))
        .recurrenceRule(mapToRecurrenceRule(task.recurrenceRule()))
        .routineOverrides(
            task.routineOverrides().stream()
                .map(GetRoutineTaskResponse::mapToOverride)
                .toList()
        )
        .build();
  }

  private static RoutineTimeInfoResponse mapToRoutineTimeInfo(RoutineTimeInfoDto info) {
    if (info == null) return null;

    return RoutineTimeInfoResponse.builder()
        .startDate(info.startDate())
        .until(info.until())
        .time(info.time())
        .weekdays(info.weekdays())
        .build();
  }

  private static RecurrenceRuleResponse mapToRecurrenceRule(RecurrenceRuleDto rule) {
    if (rule == null) return null;

    return RecurrenceRuleResponse.builder()
        .freq(rule.freq())
        .byDay(rule.byDay())
        .interval(rule.interval())
        .until(rule.until())
        .rruleStr(rule.rruleStr())
        .build();
  }

  private static RoutineTaskOverrideResponse mapToOverride(RoutineTaskOverrideDto override) {
    return RoutineTaskOverrideResponse.builder()
        .id(override.id())
        .title(override.title())
        .routineTimeInfo(mapToRoutineTimeInfo(override.routineTimeInfo()))
        .overrideType(override.overrideType())
        .overrideTypeDesc(override.overrideTypeDesc())
        .overrideDate(override.overrideDate())
        .build();
  }

  @Builder
  public record RoutineTaskResponse(
      Long id,
      Long userId,
      String title,
      String taskType,
      Integer alarmOffsetMinutes,
      RoutineTimeInfoResponse routineTimeInfo,
      RecurrenceRuleResponse recurrenceRule,
      List<RoutineTaskOverrideResponse> routineOverrides
  ) {
  }

  @Builder
  public record RoutineTimeInfoResponse(
      LocalDate startDate,
      LocalDate until,
      LocalTime time,
      Set<String> weekdays
  ) {
  }

  @Builder
  public record RecurrenceRuleResponse(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDate until,
      String rruleStr
  ) {
  }

  @Builder
  public record RoutineTaskOverrideResponse(
      Long id,
      String title,
      RoutineTimeInfoResponse routineTimeInfo,
      String overrideType,
      String overrideTypeDesc,
      LocalDate overrideDate
  ) {
  }
}
