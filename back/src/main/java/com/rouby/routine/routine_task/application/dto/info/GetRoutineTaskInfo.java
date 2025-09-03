package com.rouby.routine.routine_task.application.dto.info;

import static java.util.stream.Collectors.groupingBy;

import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.routine_task.domain.RecurrenceRule;
import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskOverride;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record GetRoutineTaskInfo(
    List<RoutineTask> routines
) {

  public static GetRoutineTaskInfo of(List<RoutineTaskWithOverrides> routineTasks) {
    return GetRoutineTaskInfo.builder()
        .routines(routineTasks.stream()
            .map(GetRoutineTaskInfo::mapToRoutineTask)
            .toList())
        .build();
  }

  public static GetRoutineTaskInfo ofWithProgress(
      List<RoutineTaskWithOverrides> routineTasks,
      List<DailyTask> dailyTasks) {

    Map<Long, List<DailyTask>> dailyTaskMap = dailyTasks.stream()
        .collect(groupingBy(DailyTask::getRoutineTaskId));

    return GetRoutineTaskInfo.builder()
        .routines(routineTasks.stream()
            .map(task -> mapToRoutineTaskWithProgress(task, dailyTaskMap.get(task.id())))
            .toList())
        .build();
  }

  private static RoutineTask mapToRoutineTaskWithProgress(
      RoutineTaskWithOverrides task,
      List<DailyTask> dailyTasks) {

    return RoutineTask.builder()
        .id(task.id())
        .userId(task.userId())
        .title(task.title())
        .taskType(task.taskType().name())
        .alarmOffsetMinutes(task.alarmOffsetType() == null ? null : task.alarmOffsetType().getMinutes())
        .routineTimeInfo(mapToRoutineTimeInfo(task.routineTimeInfo()))
        .recurrenceRule(mapToRecurrenceRule(task.recurrenceRule()))
        .routineOverrides(
            task.overrides().stream()
                .map(GetRoutineTaskInfo::mapToRoutineOverride)
                .toList()
        )
        .dailyProgress(dailyTasks != null
            ? dailyTasks.stream()
            .map(GetRoutineTaskInfo::mapToDailyProgress)
            .sorted(Comparator.comparing(DailyProgressDto::taskDate))
            .toList()
            : Collections.emptyList())
        .build();
  }

  private static DailyProgressDto mapToDailyProgress(DailyTask dailyTask) {
    return DailyProgressDto.builder()
        .dailyTaskId(dailyTask.getId())
        .taskDate(dailyTask.getTaskDate())
        .currentValue(dailyTask.getCurrentValue())
        .build();
  }

  private static RoutineTask mapToRoutineTask(RoutineTaskWithOverrides task) {
    return RoutineTask.builder()
        .id(task.id())
        .userId(task.userId())
        .title(task.title())
        .taskType(task.taskType().name())
        .alarmOffsetMinutes(task.alarmOffsetType() == null ? null : task.alarmOffsetType().getMinutes())
        .routineTimeInfo(mapToRoutineTimeInfo(task.routineTimeInfo()))
        .recurrenceRule(mapToRecurrenceRule(task.recurrenceRule()))
        .routineOverrides(
            task.overrides().stream()
                .map(GetRoutineTaskInfo::mapToRoutineOverride)
                .toList()
        )
        .build();
  }

  private static RoutineTimeInfoDto mapToRoutineTimeInfo(RoutineTimeInfo info) {
    if (info == null) return null;

    return RoutineTimeInfoDto.builder()
        .startDate(info.getStartDate())
        .untilDate(info.getUntilDate())
        .time(info.getTime())
        .weekdays(info.getWeekdays() != null
            ? info.getWeekdays().stream()
            .map(Weekday::name)
            .collect(Collectors.toSet())
            : null)
        .build();
  }

  private static RecurrenceRuleDto mapToRecurrenceRule(RecurrenceRule rrule) {
    if (rrule == null) return null;

    return RecurrenceRuleDto.builder()
        .freq(rrule.getFreq() != null ? rrule.getFreq().name() : null)
        .interval(rrule.getInterval())
        .until(rrule.getUntil() != null ? rrule.getUntil().toLocalDate() : null)
        .byDay(rrule.getByDay() != null
            ? rrule.getByDay().stream()
            .map(Weekday::name)
            .collect(Collectors.toSet())
            : null)
        .rruleStr(rrule.toRruleString())
        .build();
  }

  private static RoutineTaskOverrideDto mapToRoutineOverride(RoutineTaskOverride override) {
    return RoutineTaskOverrideDto.builder()
        .id(override.id())
        .title(override.title())
        .routineTimeInfo(mapToRoutineTimeInfo(override.routineTimeInfo()))
        .overrideType(override.overrideType() != null ? override.overrideType().name() : null)
        .overrideTypeDesc(override.overrideType() != null ? override.overrideType().getDesc() : null)
        .overrideDate(override.overrideDate())
        .build();
  }



  @Builder
  public record RoutineTask(
      Long id,
      Long userId,
      String title,
      String taskType,
      Integer alarmOffsetMinutes,
      RoutineTimeInfoDto routineTimeInfo,
      RecurrenceRuleDto recurrenceRule,
      List<RoutineTaskOverrideDto> routineOverrides,
      List<DailyProgressDto> dailyProgress
  ) {
  }

  @Builder
  public record RoutineTimeInfoDto(
      LocalDate startDate,
      LocalDate untilDate,
      LocalTime time,
      Set<String> weekdays
  ) {
  }

  @Builder
  public record RecurrenceRuleDto(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDate until,
      String rruleStr
  ) {
  }

  @Builder
  public record RoutineTaskOverrideDto(
      Long id,
      String title,
      RoutineTimeInfoDto routineTimeInfo,
      String overrideType,
      String overrideTypeDesc,
      LocalDate overrideDate
  ) {
  }

  @Builder
  public record DailyProgressDto(
      Long dailyTaskId,
      LocalDate taskDate,
      Integer currentValue
  ) {
  }
}
