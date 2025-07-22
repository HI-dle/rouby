package com.rouby.schedule.fixture;

import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest;
import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest.RecurrenceRuleRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateScheduleRequestFixture {

  public static CreateScheduleRequest getSuccessRequest() {
    return CreateScheduleRequest.builder()
        .title("하이들 모임!")
        .memo("뭉티기 먹을 것!")
        .alarmOffsetMinutes(1440)
        .startAt(LocalDate.now().plusDays(14).atTime(10, 30))
        .endAt(LocalDate.now().plusDays(15).atTime(22,30))
        .routineOffsetDays(3)
        .recurrenceRule(RecurrenceRuleRequest.builder()
            .freq("MONTHLY")
            .interval(1)
            .byDay("MO,TH")
            .until(LocalDateTime.of(2025, 12, 30, 0, 0))
            .build())
        .build();
  }

  public static CreateScheduleRequest getAlarmOffsetFailedRequest() {
    return CreateScheduleRequest.builder()
        .title("하이들 모임!")
        .memo("뭉티기 먹을 것!")
        .alarmOffsetMinutes(1340)
        .startAt(LocalDate.now().plusDays(14).atTime(10, 30))
        .endAt(LocalDate.now().plusDays(14).atTime(22,30))
        .routineOffsetDays(3)
        .recurrenceRule(RecurrenceRuleRequest.builder()
            .freq("MONTHLY")
            .interval(1)
            .byDay("MO")
            .until(LocalDateTime.of(2025, 12, 30, 0, 0))
            .build()
        )
        .build();
  }
}
