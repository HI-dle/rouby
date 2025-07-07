package com.rouby.schedule.infrastructure.persistence.jpa;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJpaRepository extends ScheduleRepository,
    JpaRepository<Schedule, Long>, ScheduleJpaRepositoryCustom {

}
