package com.rouby.routine.routine_task.application.dto.command;

import com.rouby.routine.routine_task.domain.repository.search.GetRoutineTaskCriteria;
import java.time.LocalDate;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
@Builder
public record GetRoutineTaskCommand(Long userId,
                                    LocalDate fromDate,
                                    LocalDate toDate) {

  public GetRoutineTaskCriteria toCriteria() {
    return GetRoutineTaskCriteria.builder()
        .userId(userId)
        .fromDate(fromDate)
        .toDate(toDate)
        .build();
  }

}
