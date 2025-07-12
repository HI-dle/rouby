package com.rouby.routine.infrastructure.persistence.jpa;

import com.rouby.routine.domain.RoutineTask;
import com.rouby.routine.domain.repository.RoutineTaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineTaskJpaRepository extends RoutineTaskRepository,
    JpaRepository<RoutineTask, Long>, RoutineTaskJpaRepositoryCustom {

}
