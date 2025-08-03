package com.rouby.routine.routine_task.domain.repository.search;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
@Builder
public record GetRoutineTaskCriteria(Long userId,
                                     LocalDateTime fromDate,
                                     LocalDateTime toDate) {

}
