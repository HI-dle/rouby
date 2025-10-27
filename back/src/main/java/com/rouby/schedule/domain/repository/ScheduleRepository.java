package com.rouby.schedule.domain.repository;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.repository.criteria.GetScheduleCriteria;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

  Schedule save(Schedule entity);

  List<ScheduleWithOverrides> findSchedulesByCriteria(GetScheduleCriteria criteria);

  <S extends Schedule> Iterable<S> saveAll(Iterable<S> entities);

  Optional<Schedule> findById(Long id);

  Optional<Schedule> findByIdAndUserId(Long id, Long userId);

  void bulkCancel(Long id, LocalDateTime untilAt);
}
