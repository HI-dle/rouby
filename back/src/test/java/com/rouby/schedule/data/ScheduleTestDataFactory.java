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

  private static final int MAX_USER_COUNT = 1_000;
  private static final int OVERRIDE_FREQUENCY = 4; // 4개 중 1개에 override 생성
  private static final int OVERRIDES_PER_SCHEDULE = 10;

  public static List<Schedule> generateTestSchedules(int offset, int count,
      List<Long> scheduleIds, int startIdx) {
    List<Schedule> schedulesResult = new ArrayList<>(
        count + count / OVERRIDE_FREQUENCY * OVERRIDES_PER_SCHEDULE);

    for (int i = offset; i < offset + count; i++) {

      // 부모 Schedule 생성
      Schedule schedule = createSchedule(i, scheduleIds.get(startIdx++));
      schedulesResult.add(schedule);

      // 자식 Schedule (예외) 생성
      if (i % OVERRIDE_FREQUENCY == 0) {
        for (int j = 0; j < OVERRIDES_PER_SCHEDULE; j++) {
          Long childId = scheduleIds.get(startIdx++);
          Schedule override = createScheduleOverride(schedule, i, j, childId);
          schedulesResult.add(override);
        }
      }
    }

    return schedulesResult;
  }

  private static Schedule createSchedule(int index, Long id) {
    long userId = (index % MAX_USER_COUNT) + 1;

    Schedule schedule = Schedule.builder()
        .userId(userId)
        .title("테스트 일정 " + index)
        .memo("자동 생성된 메모 " + index)
        .period(Period.builder()
            .startAt(LocalDate.now().minusMonths(2).atStartOfDay().plusDays(index / MAX_USER_COUNT))
            .endAt(
                LocalDate.now().minusMonths(2).atStartOfDay().plusDays(index / MAX_USER_COUNT + 1))
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

    ReflectionTestUtils.setField(schedule, "id", id);
    ReflectionTestUtils.setField(schedule, "routineOffsetDays", 3);
    return schedule;
  }

  private static Schedule createScheduleOverride(Schedule parent, int index, int offset, Long id) {
    Schedule override = Schedule.builder()
        .userId(parent.getUserId())
        .title("예외 일정 " + index + "-" + offset)
        .memo("예외 메모")
        .period(Period.builder()
            .startAt(parent.getPeriod().getStartAt().plusWeeks(4 + offset).plusDays(1))
            .endAt(parent.getPeriod().getEndAt().plusWeeks(4 + offset).plusDays(1))
            .build()
        )
        .alarmOffsetMinutes(60)
        .build();

    ReflectionTestUtils.setField(override, "id", id);
    ReflectionTestUtils.setField(override, "overrideInfo", OverrideInfo.builder()
        .overrideDate(parent.getPeriod().getStartAt().plusWeeks(4 + offset).toLocalDate())
        .overrideType(OverrideType.MODIFIED)
        .build()
    );
    ReflectionTestUtils.setField(override, "routineOffsetDays", 3);
    ReflectionTestUtils.setField(override, "parentSchedule", parent);

    return override;
  }

  public static int getCountSchedules(int count) {
    int overrideCount = (count / OVERRIDE_FREQUENCY) * OVERRIDES_PER_SCHEDULE;
    return count + overrideCount;
  }
}
