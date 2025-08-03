package com.rouby.routine.routine_task.domain.repository.search;

import java.time.LocalDate;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 31.
 */
public record DailyTaskResult(LocalDate localDate,
                              Integer currentValue) {

}
