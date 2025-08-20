package com.rouby.routine.routine_task.domain.repository.search;

import java.time.LocalDate;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
@Builder
public record GetRoutineTaskCriteria(Long userId,
                                     LocalDate fromDate,
                                     LocalDate toDate) {

}
