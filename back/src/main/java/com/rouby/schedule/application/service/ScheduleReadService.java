package com.rouby.schedule.application.service;

import com.rouby.common.utils.JsonHelper;
import com.rouby.schedule.application.dto.info.SchedulesInfo;
import com.rouby.schedule.application.dto.info.SchedulesSummaryInfo;
import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import com.rouby.schedule.application.exception.ScheduleErrorCode;
import com.rouby.schedule.application.exception.ScheduleException;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleReadService {

  private final ScheduleRepository scheduleRepository;
  private final JsonHelper jsonHelper;

  public SchedulesInfo findSchedulesBy(GetScheduleQuery query) {
    try {
      return SchedulesInfo.of(scheduleRepository.findSchedulesByCriteria(query.toCriteria()));
    } catch (IllegalArgumentException e) {
      throw ScheduleException.of(ScheduleErrorCode.SCHEDULE_INVALID_REQUEST, e.getMessage());
    }
  }

  public String findSummarySchedulesJsonBy(GetScheduleQuery query) {
    try {
      return jsonHelper.toJson(
          SchedulesSummaryInfo.of(scheduleRepository.findSchedulesByCriteria(query.toCriteria())));
    } catch (IllegalArgumentException e) {
      throw ScheduleException.of(ScheduleErrorCode.SCHEDULE_INVALID_REQUEST, e.getMessage());
    }
  }
}
