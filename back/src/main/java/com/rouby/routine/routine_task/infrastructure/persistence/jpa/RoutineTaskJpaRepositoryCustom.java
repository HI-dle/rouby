package com.rouby.routine.routine_task.infrastructure.persistence.jpa;

import com.rouby.routine.routine_task.domain.repository.search.GetRoutineTaskCriteria;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import java.util.List;

public interface RoutineTaskJpaRepositoryCustom {

  List<RoutineTaskWithOverrides> findRoutineTaskByCriteria(GetRoutineTaskCriteria getRoutineTaskCriteria);

}
