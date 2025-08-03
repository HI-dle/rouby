package com.rouby.routine.routine_task.infrastructure.persistence.jpa;

import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskResult;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineTaskJpaRepository extends RoutineTaskRepository,
    JpaRepository<RoutineTask, Long>, RoutineTaskJpaRepositoryCustom {

  Optional<RoutineTask> findByIdAndDeletedAtIsNull(Long id);

  List<RoutineTaskResult> findOneMonthByUserId(Long userId, LocalDate startDate, LocalDate endDate);
}
