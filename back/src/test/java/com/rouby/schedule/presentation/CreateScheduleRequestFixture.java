package com.rouby.schedule.presentation;

import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest;
import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest.RecurrenceRuleRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CreateScheduleRequestFixture {

  public static CreateScheduleRequest getSuccessRequest() {
    return CreateScheduleRequest.builder()
        .title("하이들 모임!")
        .memo("뭉티기 먹을 것!")
        .alarmOffsetMinutes(1440)
        .startDate(LocalDate.now().plusDays(14))
        .startTime(LocalTime.of(10, 30))
        .endDate(LocalDate.now().plusDays(14))
        .endTime(LocalTime.of(22, 30))
        .routineActivateDate(LocalDate.now())
        .recurrenceRule(RecurrenceRuleRequest.builder()
            .freq("MONTHLY")
            .interval(1)
            .byDay("MO")
            .until(
                ZonedDateTime.of(
                    LocalDateTime.of(2025, 12, 30, 0, 0),
                    ZoneId.of("Asia/Seoul"))
            )
            .build())
        .build();
  }

  public static CreateScheduleRequest getAlarmOffsetFailedRequest() {
    return CreateScheduleRequest.builder()
        .title("하이들 모임!")
        .memo("뭉티기 먹을 것!")
        .alarmOffsetMinutes(1340)
        .startDate(LocalDate.now().plusDays(14))
        .startTime(LocalTime.of(10, 30))
        .endDate(LocalDate.now().plusDays(14))
        .endTime(LocalTime.of(22, 30))
        .routineActivateDate(LocalDate.now())
        .recurrenceRule(RecurrenceRuleRequest.builder()
            .freq("MONTHLY")
            .interval(1)
            .byDay("MO")
            .until(ZonedDateTime.of(
                LocalDateTime.of(2025, 12, 30, 0, 0),
                ZoneId.of("Asia/Seoul"))
            )
            .build())
        .build();
  }
}
