package com.rouby.routine.routine_task.infrastructure.persistence.jpa.dto;

import java.util.List;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 31.
 */
public record RoutineTaskFetched(Long routineTaskId,
                                 String title,
                                 String taskType,
                                 Integer targetValue,
                                 List<DailyTaskFetched> dailyTasks) {

}
