package com.rouby.schedule.application.service;


import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.application.dto.command.UpdateScheduleCommand;
import com.rouby.schedule.application.exception.ScheduleErrorCode;
import com.rouby.schedule.application.exception.ScheduleException;
import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.enums.AlarmOffsetType;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import com.rouby.schedule.domain.vo.Period;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScheduleWriteService {

  private final ScheduleRepository scheduleRepository;

  public Long createSchedule(Long userId, CreateScheduleCommand command) {

    try {
      Schedule schedule = command.toEntityWithUserId(userId);
      scheduleRepository.save(schedule);
      return schedule.getId();

    } catch (IllegalArgumentException e) {
      throw ScheduleException.of(ScheduleErrorCode.SCHEDULE_INVALID_REQUEST, e.getMessage());
    }
  }

  @Transactional
  public Long updateSchedule(Long userId, UpdateScheduleCommand command) {
    Schedule schedule = null;
    log.info("originId = target 분기 처리 전");
    if (!Objects.equals(command.parentScheduleId(), command.targetScheduleId())) {
      schedule = scheduleRepository.findById(command.targetScheduleId())
          .orElseThrow(() -> ScheduleException.from(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

      schedule.validateUpdatable();
      Period period = Period.builder()
          .startAt(command.startAt())
          .endAt(command.endAt())
          .build();

      AlarmOffsetType alarmOffsetType = AlarmOffsetType.parse(command.alarmOffsetMinutes());
      schedule.update(command.title(), command.memo(), period, alarmOffsetType);
      return schedule.getId();
    } else {
      Schedule parentSchedule = scheduleRepository.findById(command.parentScheduleId())
          .orElseThrow(() -> ScheduleException.from(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
      schedule = command.toEntityWithUserId(userId, parentSchedule);
      Schedule savedSchedule = scheduleRepository.save(schedule);
      return savedSchedule.getId();
    }
  }
}
