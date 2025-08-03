package com.rouby.routine.routine_task.domain.repository.search;

import java.util.List;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 31.
 */
public record RoutineTaskResult(Long routineTaskId,
                                String title,
                                String taskType,
                                Integer targetValue,
                                List<DailyTaskResult> dailyTasks) {

}
