package com.rouby.routine.routine_task.domain.repository.search;

import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.OverrideType;
import java.time.LocalDate;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 03.
 */
public record RoutineTaskOverride(Long id,
                                  String title,
                                  Integer targetValue,
                                  RoutineTimeInfo routineTimeInfo,
                                  OverrideType overrideType,
                                  LocalDate overrideDate) {

}
