package com.rouby.routine.routine_task.infrastructure.persistence.jpa.dto;

import java.time.LocalDate;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 31.
 */
public record DailyTaskFetched(LocalDate localDate,
                               Integer currentValue) {

}
