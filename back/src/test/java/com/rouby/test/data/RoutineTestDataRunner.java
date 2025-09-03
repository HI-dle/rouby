package com.rouby.test.data;

import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.daily_task.domain.repository.DailyTaskRepository;
import com.rouby.routine.routine_task.domain.RecurrenceRule;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.AlarmOffsetType;
import com.rouby.routine.routine_task.domain.enums.Freq;
import com.rouby.routine.routine_task.domain.enums.TaskType;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 04.
 */
@SpringBootTest
@Disabled
public class RoutineTestDataRunner {

  @Autowired
  private RoutineTaskRepository routineTaskRepository;

  @Autowired
  private DailyTaskRepository dailyTaskRepository;

  private static final Long TEST_USER_ID = 1L;

  @Test
  public void run() {
    LocalDate startDate = LocalDate.of(2025, 8, 1);
    LocalDate endDate = LocalDate.of(2025, 8, 30);

    Set<Weekday> weekdaySet = Set.of(Weekday.FR, Weekday.SA, Weekday.SU);

    RecurrenceRule recurrenceRule = RecurrenceRule.builder()
        .freq(Freq.WEEKLY)
        .interval(1)
        .byDay(weekdaySet)
        .until(endDate.atTime(LocalTime.ofSecondOfDay(1L)))
        .build();

    for (int i = 1; i <= 3; i++) {
      RoutineTask routine = RoutineTask.builder()
          .userId(TEST_USER_ID)
          .title("루틴 태스크 " + i)
          .taskType(TaskType.COUNT)
          .targetValue(100)
          .alarmOffsetType(AlarmOffsetType.M_5)
          .recurrenceRule(recurrenceRule)
          .routineTimeInfo(RoutineTimeInfo.builder()
              .startDate(startDate)
              .untilDate(endDate)
              .time(LocalTime.of(8 + i, 0)) // 8:00, 9:00, 10:00 등
              .weekdays(Set.of(Weekday.WE, Weekday.SA))
              .build())
          .build();

      routineTaskRepository.save(routine);

      List<DayOfWeek> byDays = weekdaySet.stream()
          .map(Weekday::getDayOfWeek)
          .toList();

      generateDailyTasks(routine.getId(), startDate, endDate, byDays);
    }

  }

  private void generateDailyTasks(Long routineTaskId, LocalDate startDate, LocalDate endDate, List<DayOfWeek> byDays) {
    List<DailyTask> dailyTasks = new ArrayList<>();

    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
      if (byDays.contains(date.getDayOfWeek())) {
        dailyTasks.add(DailyTask.create(routineTaskId, date, 0));
      }
    }

    dailyTaskRepository.saveAll(dailyTasks);
  }
}
