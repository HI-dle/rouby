package com.rouby.schedule.domain.repository;

import com.rouby.schedule.domain.entity.Schedule;

public interface ScheduleRepository {

  Schedule save(Schedule entity);
}
