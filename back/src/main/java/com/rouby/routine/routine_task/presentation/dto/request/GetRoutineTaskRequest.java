package com.rouby.routine.routine_task.presentation.dto.request;

import com.rouby.routine.routine_task.application.dto.command.GetRoutineTaskCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
public record GetRoutineTaskRequest(@NotNull LocalDate fromDate,
                                    @NotNull LocalDate toDate
                                    ) {

  public GetRoutineTaskCommand toCommand(Long userId) {
    if (!fromDate.isBefore(toDate)) {
      throw new IllegalArgumentException("일정 조회 시작일시가 종료일시보다 나중일 수 없습니다.");
    }

    return GetRoutineTaskCommand.builder()
        .userId(userId)
        .fromDate(fromDate)
        .toDate(toDate)
        .build();
  }

}
