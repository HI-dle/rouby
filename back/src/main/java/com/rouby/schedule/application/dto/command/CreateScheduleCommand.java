package com.rouby.schedule.application.dto.command;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;
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
        .recurrenceRule(recurrenceRule.toDomain())
        .build();
  }

  @Builder
  public record RecurrenceRuleCommand(
      String freq,
      String byDay,
      Integer interval,
      LocalDateTime until
  ) {

    public RecurrenceRule toDomain() {

      return RecurrenceRule.builder()
          .freq(Freq.valueOf(freq))
          .byDay(Arrays.stream(byDay.split(",")).map(ByDay::valueOf).collect(Collectors.toSet()))
          .interval(interval)
          .until(until)
          .build();
    }
  }
}
