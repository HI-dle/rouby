package com.rouby.schedule.data;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.enums.Freq;
import com.rouby.schedule.domain.enums.OverrideType;
import com.rouby.schedule.domain.vo.OverrideInfo;
import com.rouby.schedule.domain.vo.Period;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class ScheduleTestDataFactory {

  public static List<Schedule> generateTestSchedules(int count) {
    List<Schedule> schedules = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      schedules.add(createSchedule(i));
    }
    return schedules;
  }

  private static Schedule createSchedule(int index) {
    Schedule schedule = Schedule.builder()
        .userId((long) (index % 5 + 1))
        .title("테스트 일정 " + index)
        .memo("자동 생성된 메모 " + index)
        .period(Period.builder()
            .startAt(LocalDate.now().minusMonths(2).atStartOfDay().plusDays(index))
            .endAt(LocalDate.now().minusMonths(2).atStartOfDay().plusDays(index + 1))
            .build())
        .alarmOffsetMinutes(60)
        .recurrenceRule(index % 2 == 0
            ? RecurrenceRule.builder()
            .freq(Freq.WEEKLY)
            .interval(1)
            .build()
            : null
        )
        .build();

    ReflectionTestUtils.setField(schedule, "routineOffsetDays", 3);
    if (index % 4 == 0) {
      ReflectionTestUtils.setField(schedule, "children", List.of(createScheduleOverride(schedule, index)));
    }
    return schedule;
  }

  private static Schedule createScheduleOverride(Schedule parent, int index) {
    Schedule override = Schedule.builder()
        .userId(parent.getUserId())
        .title("예외 일정 " + index)
        .memo("예외 메모")
        .period(Period.builder()
            .startAt(parent.getPeriod().getStartAt().plusWeeks(4).plusDays(1))
            .endAt(parent.getPeriod().getEndAt().plusWeeks(4).plusDays(1))
            .build()
        )
        .alarmOffsetMinutes(60)
        .build();

    ReflectionTestUtils.setField(override, "overrideInfo", OverrideInfo.builder()
        .overrideDate(parent.getPeriod().getStartAt().plusWeeks(4).toLocalDate())
        .overrideType(OverrideType.MODIFIED)
        .build()
    );
    ReflectionTestUtils.setField(override, "routineOffsetDays", 3);
    ReflectionTestUtils.setField(override, "parentSchedule", parent);

    return override;
  }
}
