package com.rouby.assistant.feedback.application.dto;

import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record RoutineTasksInfoForFeedback(
    List<RoutineTask> routines
) {

  public static RoutineTasksInfoForFeedback from(GetRoutineTaskInfo routineTaskInfo) {

    return RoutineTasksInfoForFeedback.builder()
        .routines(
            routineTaskInfo.routines() == null
                ? List.of()
                : routineTaskInfo.routines().stream().map(RoutineTask::from).toList())
        .build();
  }

  @Builder
  public record RoutineTask(
      Long id,
      Long userId,
      String title,
      String taskType,
      Integer targetValue,
      Integer alarmOffsetMinutes,
      RoutineTimeInfoDto routineTimeInfo,
      RecurrenceRuleDto recurrenceRule,
      List<RoutineTaskOverrideDto> routineOverrides,
      List<DailyProgressDto> dailyProgress
  ) {

    public static RoutineTask from(GetRoutineTaskInfo.RoutineTask routineTask) {

      return RoutineTask.builder()
          .id(routineTask.id())
          .userId(routineTask.userId())
          .title(routineTask.title())
          .taskType(routineTask.taskType())
          .targetValue(routineTask.targetValue())
          .alarmOffsetMinutes(routineTask.alarmOffsetMinutes())
          .routineTimeInfo(
              routineTask.routineTimeInfo() == null
                  ? null
                  : RoutineTimeInfoDto.from(routineTask.routineTimeInfo()))
          .recurrenceRule(
              routineTask.recurrenceRule() == null
                  ? null
                  : RecurrenceRuleDto.from(routineTask.recurrenceRule()))
          .routineOverrides(
              routineTask.routineOverrides() == null
                  ? List.of()
                  : routineTask.routineOverrides().stream()
                      .map(RoutineTaskOverrideDto::from)
                      .toList())
          .dailyProgress(
              routineTask.dailyProgress() == null
                  ? List.of()
                  : routineTask.dailyProgress().stream()
                      .map(DailyProgressDto::from)
                      .toList())
          .build();
    }
  }

  @Builder
  public record RoutineTimeInfoDto(
      LocalDate startDate,
      LocalTime time,
      LocalDate untilDate,
      Set<String> weekdays
  ) {

    public static RoutineTimeInfoDto from(
        GetRoutineTaskInfo.RoutineTimeInfoDto routineTimeInfoDto) {

      if (routineTimeInfoDto == null) return null;

      return RoutineTimeInfoDto.builder()
          .startDate(routineTimeInfoDto.startDate())
          .time(routineTimeInfoDto.time())
          .untilDate(routineTimeInfoDto.untilDate())
          .weekdays(routineTimeInfoDto.weekdays())
          .build();
    }
  }

  @Builder
  public record RecurrenceRuleDto(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDate until,
      String rruleStr
  ) {

    public static RecurrenceRuleDto from(GetRoutineTaskInfo.RecurrenceRuleDto recurrenceRuleDto) {

      if (recurrenceRuleDto == null) return null;

      return RecurrenceRuleDto.builder()
          .freq(recurrenceRuleDto.freq())
          .byDay(recurrenceRuleDto.byDay())
          .interval(recurrenceRuleDto.interval())
          .until(recurrenceRuleDto.until())
          .rruleStr(recurrenceRuleDto.rruleStr())
          .build();
    }
  }

  @Builder
  public record RoutineTaskOverrideDto(
      Long id,
      String title,
      Integer targetValue,
      GetRoutineTaskInfo.RoutineTimeInfoDto routineTimeInfo,
      String overrideType,
      String overrideTypeDesc,
      LocalDate overrideDate
  ) {

    public static RoutineTaskOverrideDto from(GetRoutineTaskInfo.RoutineTaskOverrideDto routineTaskOverrideDto) {

      return RoutineTaskOverrideDto.builder()
          .id(routineTaskOverrideDto.id())
          .title(routineTaskOverrideDto.title())
          .targetValue(routineTaskOverrideDto.targetValue())
          .routineTimeInfo(routineTaskOverrideDto.routineTimeInfo())
          .overrideType(routineTaskOverrideDto.overrideType())
          .overrideTypeDesc(routineTaskOverrideDto.overrideTypeDesc())
          .overrideDate(routineTaskOverrideDto.overrideDate())
          .build();
    }
  }

  @Builder
  public record DailyProgressDto(
      Long dailyTaskId,
      LocalDate taskDate,
      Integer currentValue
  ) {

    public static DailyProgressDto from(GetRoutineTaskInfo.DailyProgressDto dailyProgressDto) {

      return DailyProgressDto.builder()
          .dailyTaskId(dailyProgressDto.dailyTaskId())
          .taskDate(dailyProgressDto.taskDate())
          .currentValue(dailyProgressDto.currentValue())
          .build();
    }
  }
}
