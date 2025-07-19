package com.rouby.schedule.application.facade;

import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.application.dto.info.SchedulesInfo;
import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import com.rouby.schedule.application.service.ScheduleReadService;
import com.rouby.schedule.application.service.ScheduleWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleFacade {

  private final ScheduleWriteService scheduleWriteService;
  private final ScheduleReadService scheduleReadService;

  public Long createSchedule(Long userId, CreateScheduleCommand command) {
    return scheduleWriteService.createSchedule(userId, command);
  }

  public SchedulesInfo getSchedules(GetScheduleQuery query) {
    return scheduleReadService.findSchedulesBy(query);
  }
}
