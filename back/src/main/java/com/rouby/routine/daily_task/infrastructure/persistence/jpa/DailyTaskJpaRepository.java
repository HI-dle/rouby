package com.rouby.routine.daily_task.infrastructure.persistence.jpa;

import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.daily_task.domain.repository.DailyTaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyTaskJpaRepository extends DailyTaskRepository,
    JpaRepository<DailyTask, Long>, DailyTaskJpaRepositoryCustom {
}
