package com.rouby.routine.routine_task.application.dto.command;

import com.rouby.routine.routine_task.domain.repository.search.GetRoutineTaskCriteria;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
@Builder
public record GetRoutineTaskCommand(Long userId,
                                    LocalDateTime fromDate,
                                    LocalDateTime toDate) {

  public GetRoutineTaskCriteria toCriteria() {
    return GetRoutineTaskCriteria.builder()
        .userId(userId)
        .fromDate(fromDate)
        .toDate(toDate)
        .build();
  }

}
