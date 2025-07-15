package com.rouby.routine.routine_task.infrastructure.persistence.jpa;

import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineTaskJpaRepository extends RoutineTaskRepository,
    JpaRepository<RoutineTask, Long>, RoutineTaskJpaRepositoryCustom {

}
