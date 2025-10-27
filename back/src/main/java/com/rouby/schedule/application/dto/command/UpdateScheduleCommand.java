package com.rouby.schedule.application.dto.command;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.entity.enums.OverrideType;
import com.rouby.schedule.domain.entity.vo.OverrideInfo;
import com.rouby.schedule.domain.entity.vo.Period;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UpdateScheduleCommand(
    Long parentScheduleId,
    Long targetScheduleId,
    String title,
    String memo,
    Integer alarmOffsetMinutes,
    LocalDate overrideDate,
    LocalDateTime startAt,
    LocalDateTime endAt
) {

  public Schedule toEntityWithUserId(Long userId, Schedule parentSchedule) {
    return Schedule.createByOverride(
        userId,
        title,
        memo,
        Period.builder().startAt(startAt).endAt(endAt).build(),
        alarmOffsetMinutes,
        parentSchedule,
        OverrideInfo.builder()
            .overrideDate(overrideDate)
            .overrideType(OverrideType.MODIFIED)
            .build());
  }
}
