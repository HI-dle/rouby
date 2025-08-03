package com.rouby.routine.routine_task.application.dto.command;

import com.rouby.routine.routine_task.domain.RecurrenceRule;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.AlarmOffsetType;
import com.rouby.routine.routine_task.domain.enums.TaskType;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record CreateRoutineTaskCommand(
    Long writerId,
    String title,
    CommandTaskType taskType,
    Integer targetValue,
    Integer alarmOffsetMinutes,
    LocalDate startDate,
    LocalTime time,
    Set<CommandWeekday> byDays,
    LocalDateTime until
) {

  public RoutineTask toEntity(){
    return RoutineTask.builder()
        .userId(writerId)
        .title(title)
        .taskType(TaskType.valueOf(taskType.name()))
        .targetValue(targetValue)
        .alarmOffsetType(AlarmOffsetType.parse(alarmOffsetMinutes))
        .recurrenceRule(RecurrenceRule.builder()
            .byDay(byDays.stream().map(x -> Weekday.valueOf(x.name())).collect(Collectors.toSet()))
            .until(until)
            .build())
        .routineTimeInfo(RoutineTimeInfo.builder()
            .startDate(startDate)
            .until(until.toLocalDate())
            .time(time)
            .weekdays(byDays.stream().map(x -> Weekday.valueOf(x.name())).collect(Collectors.toSet()))
            .build())
        .build();
  }

  public enum CommandTaskType {
    COUNT,
    MINUTES,
    CHECK;
  }

  public enum CommandWeekday {
    MO, TU, WE, TH, FR, SA, SU;
  }
}
