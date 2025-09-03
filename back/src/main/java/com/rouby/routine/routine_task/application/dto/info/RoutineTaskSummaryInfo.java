package com.rouby.routine.routine_task.application.dto.info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskOverride;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record RoutineTaskSummaryInfo(
    List<RoutineSummaryInfo> routines
) {
  public static RoutineTaskSummaryInfo of(List<RoutineTaskWithOverrides> routineTasks) {
    return RoutineTaskSummaryInfo.builder()
        .routines(safeList(routineTasks).stream()
            .map(RoutineTaskSummaryInfo::mapToRoutineSummary)
            .toList())
        .build();
  }

  public static <T> List<T> safeList(List<T> list) {
    return (list == null) ? List.of() : list;
  }

  private static RoutineSummaryInfo mapToRoutineSummary(RoutineTaskWithOverrides task) {
    return RoutineSummaryInfo.builder()
        .title(task.title())
        .taskType(task.taskType().name())
        .timeInfo(mapToRoutineTimeInfo(task.routineTimeInfo()))
        .recurrenceRule(
            task.recurrenceRule() != null ? task.recurrenceRule().toRruleString() : "NONE")
        .overrides(safeList(task.overrides()).stream()
            .map(RoutineTaskSummaryInfo::mapToRoutineOverrideSummary)
            .toList())
        .build();
  }

  private static RoutineOverrideSummary mapToRoutineOverrideSummary(RoutineTaskOverride override) {
    return RoutineOverrideSummary.builder()
        .title(override.title())
        .overrideType(override.overrideType() != null ? override.overrideType().name() : null)
        .overrideTypeDesc(
            override.overrideType() != null ? override.overrideType().getDesc() : null)
        .overrideDate(override.overrideDate())
        .timeInfo(mapToRoutineTimeInfo(override.routineTimeInfo()))
        .build();
  }

  private static RoutineTimeInfoSummary mapToRoutineTimeInfo(RoutineTimeInfo info) {
    return RoutineTimeInfoSummary.builder()
        .startDate(info.getStartDate())
        .until(info.getUntil())
        .time(info.getTime())
        .weekdays(info.getWeekdays() != null
            ? info.getWeekdays().stream().map(Weekday::name).collect(Collectors.toSet())
            : null)
        .build();
  }

  @Builder
  public record RoutineSummaryInfo(
      String title,
      String taskType,
      RoutineTimeInfoSummary timeInfo,
      String recurrenceRule,
      List<RoutineOverrideSummary> overrides
  ) {

  }

  @Builder
  public record RoutineOverrideSummary(
      String title,
      String overrideType,
      String overrideTypeDesc,
      @JsonSerialize(using = LocalDateSerializer.class)
      LocalDate overrideDate,
      RoutineTimeInfoSummary timeInfo
  ) {

  }

  @Builder
  public record RoutineTimeInfoSummary(
      @JsonSerialize(using = LocalDateSerializer.class)
      LocalDate startDate,
      @JsonSerialize(using = LocalDateSerializer.class)
      LocalDate until,
      @JsonSerialize(using = LocalTimeSerializer.class)
      LocalTime time,
      Set<String> weekdays
  ) {

  }
}
