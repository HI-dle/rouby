package com.rouby.schedule.application.facade;

import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.application.service.ScheduleWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleFacade {

  private final ScheduleWriteService scheduleWriteService;

  public Long createSchedule(Long userId, CreateScheduleCommand command) {
    return scheduleWriteService.createSchedule(userId, command);
  }
}
