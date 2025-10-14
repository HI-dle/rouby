package com.rouby.routine.routine_task.presentation.dto.response;

import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo.DailyProgressDto;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo.RecurrenceRuleDto;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo.RoutineTask;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo.RoutineTaskOverrideDto;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo.RoutineTimeInfoDto;
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
        .targetValue(task.targetValue())
        .taskType(task.taskType())
        .alarmOffsetMinutes(task.alarmOffsetMinutes())
        .routineTimeInfo(mapToRoutineTimeInfo(task.routineTimeInfo()))
        .recurrenceRule(mapToRecurrenceRule(task.recurrenceRule()))
        .routineOverrides(
            task.routineOverrides().stream()
                .map(GetRoutineTaskResponse::mapToOverride)
                .toList()
        )
        .dailyProgress(
            task.dailyProgress().stream()
                .map(GetRoutineTaskResponse::mapToDailyProgress)
                .toList()
        )
        .build();
  }

  private static RoutineTimeInfoResponse mapToRoutineTimeInfo(RoutineTimeInfoDto info) {
    if (info == null) return null;

    return RoutineTimeInfoResponse.builder()
        .startDate(info.startDate())
        .untilDate(info.untilDate())
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
        .targetValue(override.targetValue())
        .routineTimeInfo(mapToRoutineTimeInfo(override.routineTimeInfo()))
        .overrideType(override.overrideType())
        .overrideTypeDesc(override.overrideTypeDesc())
        .overrideDate(override.overrideDate())
        .build();
  }

  private static DailyProgressResponse mapToDailyProgress(DailyProgressDto dto) {
    return DailyProgressResponse.builder()
        .dailyTaskId(dto.dailyTaskId())
        .taskDate(dto.taskDate())
        .currentValue(dto.currentValue())
        .build();
  }

  @Builder
  public record RoutineTaskResponse(
      Long id,
      Long userId,
      String title,
      Integer targetValue,
      String taskType,
      Integer alarmOffsetMinutes,
      RoutineTimeInfoResponse routineTimeInfo,
      RecurrenceRuleResponse recurrenceRule,
      List<RoutineTaskOverrideResponse> routineOverrides,
      List<DailyProgressResponse> dailyProgress
  ) {
  }
  @Builder
  public record DailyProgressResponse(
      Long dailyTaskId,
      LocalDate taskDate,
      Integer currentValue
  ) {
  }

  @Builder
  public record RoutineTimeInfoResponse(
      LocalDate startDate,
      LocalDate untilDate,
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
      Integer targetValue,
      RoutineTimeInfoResponse routineTimeInfo,
      String overrideType,
      String overrideTypeDesc,
      LocalDate overrideDate
  ) {
  }
}
