package com.rouby.schedule.application.service;


import com.rouby.schedule.application.dto.DeleteScheduleCommand;
import com.rouby.schedule.application.dto.DeleteScheduleFromCommand;
import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.application.dto.command.UpdateScheduleCommand;
import com.rouby.schedule.application.exception.ScheduleErrorCode;
import com.rouby.schedule.application.exception.ScheduleException;
import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.entity.enums.AlarmOffsetType;
import com.rouby.schedule.domain.entity.vo.Period;
import com.rouby.schedule.domain.repository.ScheduleRepository;
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

  @Transactional
  public void deleteSchedule(Long userId, DeleteScheduleCommand command) {
    Schedule schedule = scheduleRepository.findByIdAndUserId(command.scheduleId(), userId)
        .orElseThrow(() -> ScheduleException.from(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

    Schedule parentSchedule = schedule.getParentSchedule();

    if (parentSchedule != null) {
      schedule.cancel();
    } else {
      Schedule newSchedule = command.toEntityWithUserId(userId, schedule);
      scheduleRepository.save(newSchedule);
    }
  }

  @Transactional
  public void deleteFromSchedule(Long userId, DeleteScheduleFromCommand command) {
    Schedule schedule = scheduleRepository.findByIdAndUserId(command.scheduleId(), userId)
        .orElseThrow(() -> ScheduleException.from(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

    Schedule parentSchedule = schedule.getParentSchedule();

    if (parentSchedule != null) {
      parentSchedule.cancelFrom(command.fromAt());
      scheduleRepository.bulkCancel(parentSchedule.getId(), command.fromAt());
    } else {
      schedule.cancelFrom(command.fromAt());
      scheduleRepository.bulkCancel(schedule.getId(), command.fromAt());
    }
  }
}
