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

    try {
      final boolean createOverride = command.targetScheduleId() == null
              || Objects.equals(command.parentScheduleId(), command.targetScheduleId());
      if (createOverride) {
        Schedule parent = scheduleRepository
            .findByIdAndUserId(command.parentScheduleId(), userId)
            .orElseThrow(() -> ScheduleException.from(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        Schedule newSchedule = command.toEntityWithUserId(userId, parent);
        return scheduleRepository.save(newSchedule).getId();
      }

      Schedule schedule = scheduleRepository
          .findByIdAndUserId(command.targetScheduleId(), userId)
          .orElseThrow(() -> ScheduleException.from(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

      schedule.validateUpdatable();
      Period period = Period.builder()
          .startAt(command.startAt())
          .endAt(command.endAt())
          .build();
      AlarmOffsetType alarmOffsetType = AlarmOffsetType.parse(command.alarmOffsetMinutes());
      schedule.update(command.title(), command.memo(), period, alarmOffsetType);
      return schedule.getId();
    } catch (IllegalArgumentException e) {
      throw ScheduleException.of(ScheduleErrorCode.SCHEDULE_INVALID_REQUEST, e.getMessage());
    }
  }
}
