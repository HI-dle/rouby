package com.rouby.schedule.application.dto;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.entity.enums.OverrideType;
import com.rouby.schedule.domain.entity.vo.OverrideInfo;
import com.rouby.schedule.domain.entity.vo.Period;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DeleteScheduleCommand(
    Long scheduleId,
    LocalDate instanceDate,
    LocalDateTime startAt,
    LocalDateTime endAt
) {

  public Schedule toEntityWithUserId(Long userId, Schedule schedule) {
    return Schedule.createByOverride(
        userId,
        schedule.getTitle(),
        schedule.getMemo(),
        Period.builder()
            .startAt(startAt)
            .endAt(endAt)
            .build(),
        schedule.getAlarmOffsetType().getMinutes(),
        schedule,
        OverrideInfo.builder()
            .overrideDate(instanceDate)
            .overrideType(OverrideType.CANCELLED)
            .build());
  }
}
