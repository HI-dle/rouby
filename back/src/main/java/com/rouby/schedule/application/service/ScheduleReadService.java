package com.rouby.schedule.application.service;

import com.rouby.schedule.application.dto.info.SchedulesInfo;
import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleReadService {

  private final ScheduleRepository scheduleRepository;

  public SchedulesInfo findSchedulesBy(GetScheduleQuery query) {
    return SchedulesInfo.of(scheduleRepository.findSchedulesByCriteria(query.toCriteria()));
  }
}
