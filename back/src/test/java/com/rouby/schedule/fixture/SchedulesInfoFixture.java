package com.rouby.schedule.fixture;

import com.rouby.schedule.application.dto.info.SchedulesInfo;
import com.rouby.schedule.application.dto.info.SchedulesInfo.RecurrenceRuleInfo;
import com.rouby.schedule.application.dto.info.SchedulesInfo.ScheduleInfo;
import com.rouby.schedule.application.dto.info.SchedulesInfo.ScheduleOverrideInfo;
import java.time.LocalDate;
import java.util.List;

public class SchedulesInfoFixture {

  public static SchedulesInfo createSchedulesInfo() {
    return SchedulesInfo.builder()
        .schedules(List.of(
            ScheduleInfo.builder()
                .id(1L)
                .userId(1L)
                .title("하이들 모임")
                .memo("매주 면접 스터디")
                .startAt(LocalDate.now().plusDays(15).atStartOfDay())
                .endAt(LocalDate.now().plusDays(15).atStartOfDay())
                .routineOffsetDays(3)
                .recurrenceRule(
                    RecurrenceRuleInfo.builder()
                        .freq("WEEKLY")
                        .until(LocalDate.now().plusYears(10).atStartOfDay())
                        .interval(1)
                        .build()
                )
                .alarmOffsetMinutes(10)
                .scheduleOverrides(List.of(
                    ScheduleOverrideInfo.builder()
                        .id(1L)
                        .userId(1L)
                        .title("하이들 모임 변경")
                        .memo("또 봄")
                        .startAt(LocalDate.now().plusDays(17).plusWeeks(3).atStartOfDay())
                        .endAt(LocalDate.now().plusDays(17).plusWeeks(3).atStartOfDay())
                        .routineOffsetDays(3)
                        .alarmOffsetMinutes(60)
                        .overrideDate(LocalDate.now().plusDays(15).plusWeeks(3))
                        .overrideType("MODIFIED")
                        .build()
                ))
                .build()
        ))
        .build();
  }
}
