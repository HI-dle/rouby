package com.rouby.routine.routine_task.domain.repository.search;

import com.rouby.routine.routine_task.domain.RecurrenceRule;
import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.AlarmOffsetType;
import com.rouby.routine.routine_task.domain.enums.TaskType;
import java.util.List;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
public record RoutineTaskWithOverrides(Long id,
                                       Long userId,
                                       String title,
                                       TaskType taskType,
                                       Integer targetValue,
                                       RoutineTimeInfo routineTimeInfo,
                                       RecurrenceRule recurrenceRule,
                                       AlarmOffsetType alarmOffsetType,
                                       List<RoutineTaskOverride> overrides) {

}
