package com.rouby.schedule.application.dto.command;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record CreateScheduleCommand(
    String title,
    String memo,
    Integer alarmOffsetMinutes,
    LocalDate routineActivateDate,
    LocalDate startDate,
    LocalTime startTime,
    LocalDate endDate,
    LocalTime endTime,
    RecurrenceRuleCommand recurrenceRule
) {

  public Schedule toEntity() {

    return Schedule.builder()
        .title(title)
        .memo(memo)
        .alarmOffsetMinutes(alarmOffsetMinutes)
        .routineActivateDate(routineActivateDate)
        .startDate(startDate)
        .startTime(startTime)
        .endDate(endDate)
        .endTime(endTime)
        .recurrenceRule((recurrenceRule != null) ? recurrenceRule.toDomain() : null)
        .build();
  }

  @Builder
  public record RecurrenceRuleCommand(
      String freq,
      String byDay,
      Integer interval,
      ZonedDateTime until
  ) {

    public RecurrenceRule toDomain() {

      return RecurrenceRule.builder()
          .freq(Freq.parse(freq))
          .byDay(ByDay.parseStringToSet(byDay))
          .interval(interval)
          .until(until)
          .build();
    }
  }
}
