package com.rouby.schedule.application.dto.command;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import com.rouby.schedule.domain.vo.Period;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateScheduleCommand(
    String title,
    String memo,
    Integer alarmOffsetMinutes,
    LocalDate routineActivateDate,
    LocalDateTime startAt,
    LocalDateTime endAt,
    RecurrenceRuleCommand recurrenceRule
) {

  public Schedule toEntityWithUserId(Long userId) {

    RecurrenceRule recurrenceRuleDomain = (recurrenceRule != null) ? recurrenceRule.toDomain() : null;

    return Schedule.builder()
        .userId(userId)
        .title(title)
        .memo(memo)
        .alarmOffsetMinutes(alarmOffsetMinutes)
        .routineActivateDate(routineActivateDate)
        .period(Period.builder()
            .startAt(startAt)
            .endAt(endAt)
            .build())
        .recurrenceRule(recurrenceRuleDomain)
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
          .freq(Freq.parse(freq))
          .byDay(ByDay.parseStringToSet(byDay))
          .interval(interval)
          .until(until)
          .build();
    }
  }
}
