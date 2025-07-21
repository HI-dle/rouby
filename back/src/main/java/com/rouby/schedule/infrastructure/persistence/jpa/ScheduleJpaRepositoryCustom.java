package com.rouby.schedule.infrastructure.persistence.jpa;

import com.rouby.schedule.domain.repository.criteria.GetScheduleCriteria;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import java.util.List;

public interface ScheduleJpaRepositoryCustom {

  List<ScheduleWithOverrides> findSchedulesByCriteria(GetScheduleCriteria condition);
}
