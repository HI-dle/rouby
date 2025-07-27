package com.rouby.schedule.data;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.enums.Freq;
import com.rouby.schedule.domain.enums.OverrideType;
import com.rouby.schedule.domain.vo.OverrideInfo;
import com.rouby.schedule.domain.vo.Period;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.util.ReflectionTestUtils;


public class ScheduleTestDataFactory {

  private static final int MAX_USER_COUNT = 1_000;

  private static final int OVERRIDE_FREQUENCY = 4; // 4개 중 1개에 override 생성
  private static final int OVERRIDES_PER_SCHEDULE = 10;

  public static List<Schedule> generateTestSchedules(int offset, int count) {
    List<Schedule> schedulesResult = new ArrayList<>(count);
    List<Schedule> overridesResult = new ArrayList<>(count / 4 * 10);

    for (int i = offset; i < offset + count; i++) {

      Schedule schedule = createSchedule(i);

      List<Schedule> overrides = Collections.emptyList();
      if (i % OVERRIDE_FREQUENCY == 0) {
        int finalI = i;
        overrides = IntStream.range(0, OVERRIDES_PER_SCHEDULE)
            .mapToObj(j -> createScheduleOverride(schedule, finalI, j))
            .toList();
        overridesResult.addAll(overrides);
      }
      schedulesResult.add(schedule);
      schedulesResult.addAll(overrides);
    }
    return schedulesResult;
  }

  private static Schedule createSchedule(int index) {
    long userId = (index % MAX_USER_COUNT) + 1;

    Schedule schedule = Schedule.builder()
        .userId(userId)
        .title("테스트 일정 " + index)
        .memo("자동 생성된 메모 " + index)
        .period(Period.builder()
            .startAt(LocalDate.now().minusMonths(2).atStartOfDay().plusDays(index / MAX_USER_COUNT))
            .endAt(LocalDate.now().minusMonths(2).atStartOfDay().plusDays(index / MAX_USER_COUNT + 1))
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
    return schedule;
  }

  private static Schedule createScheduleOverride(Schedule parent, int index, int offset) {
    Schedule override = Schedule.builder()
        .userId(parent.getUserId())
        .title("예외 일정 " + index / MAX_USER_COUNT + "-" + offset)
        .memo("예외 메모")
        .period(Period.builder()
            .startAt(parent.getPeriod().getStartAt().plusWeeks(4 + offset).plusDays(1))
            .endAt(parent.getPeriod().getEndAt().plusWeeks(4 + offset).plusDays(1))
            .build()
        )
        .alarmOffsetMinutes(60)
        .build();

    ReflectionTestUtils.setField(override, "overrideInfo", OverrideInfo.builder()
        .overrideDate(parent.getPeriod().getStartAt().plusWeeks(4 + offset).toLocalDate())
        .overrideType(OverrideType.MODIFIED)
        .build()
    );
    ReflectionTestUtils.setField(override, "routineOffsetDays", 3);
    ReflectionTestUtils.setField(override, "parentSchedule", parent);

    return override;
  }
}
