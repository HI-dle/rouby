package com.rouby.schedule.application.service;

import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleWriteService {

  private final ScheduleRepository scheduleRepository;

  public Long createSchedule(Long userId, CreateScheduleCommand command) {

    Schedule schedule = command.toEntityWithUserId(userId);
    scheduleRepository.save(schedule);

    return schedule.getId();
  }
}
