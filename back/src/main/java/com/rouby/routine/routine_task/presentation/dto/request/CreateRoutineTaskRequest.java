package com.rouby.routine.routine_task.presentation.dto.request;

import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand;
import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand.CommandTaskType;
import com.rouby.routine.routine_task.application.dto.command.CreateRoutineTaskCommand.CommandWeekday;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateRoutineTaskRequest (
    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    @Size(max = 500, message = "제목은 최대 500자까지 입력할 수 있습니다.")
    String title,

    @NotNull(message = "태스크 타입은 필수입니다.")
    TaskType taskType,

    @NotNull(message = "목표 값은 필수입니다.")
    Integer targetValue,

    @PositiveOrZero(message = "알람 오프셋 값은 0이상이어야 합니다.")
    Integer alarmOffsetMinutes,

    @NotNull(message = "시작일은 필수입니다.")
    LocalDate startDate,

    @NotNull(message = "시작 시간은 필수입니다.")
    LocalTime time,

    @NotNull(message = "요일 리스트는 null일 수 없습니다.")
    @Size(min = 1, message = "요일은 최소 1개 이상 선택해야 합니다.")
    Set<Weekday> byDays,

    @NotNull(message = "종료일은 필수입니다.")
    LocalDateTime until
    ) {

  public CreateRoutineTaskCommand toCommand(Long writerId) {
    return CreateRoutineTaskCommand.builder()
        .writerId(writerId)
        .title(title)
        .taskType(CommandTaskType.valueOf(taskType.name()))
        .targetValue(targetValue)
        .alarmOffsetMinutes(alarmOffsetMinutes)
        .startDate(startDate)
        .time(time)
        .byDays(byDays.stream()
            .map(x -> CommandWeekday.valueOf(x.name()))
            .collect(Collectors.toSet()))
        .until(until)
        .build();
  }

  public enum TaskType {
    COUNT,
    MINUTES,
    CHECK;
  }

  public enum Weekday {
    MO, TU, WE, TH, FR, SA, SU;
  }
}
