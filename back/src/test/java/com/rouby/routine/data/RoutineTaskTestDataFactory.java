package com.rouby.routine.data;

import com.rouby.routine.routine_task.domain.OverrideInfo;
import com.rouby.routine.routine_task.domain.RecurrenceRule;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.RoutineTimeInfo;
import com.rouby.routine.routine_task.domain.enums.AlarmOffsetType;
import com.rouby.routine.routine_task.domain.enums.Freq;
import com.rouby.routine.routine_task.domain.enums.OverrideType;
import com.rouby.routine.routine_task.domain.enums.TaskType;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RoutineTaskTestDataFactory {

  private static final Random random = new Random();
  private static final String[] ROUTINE_TITLES = {
      "매일 물 2L 마시기", "운동하기", "독서 30분", "영어 공부", "명상하기",
      "비타민 섭취", "스트레칭", "일기 쓰기", "산책하기", "요가하기",
      "기타 연습", "피아노 연습", "코딩 공부", "블로그 작성", "요리하기",
      "단백질 섭취", "체중 측정", "수면 8시간", "금연", "금주"
  };

  private static final Long[] USER_IDS = {1L, 2L, 3L, 4L, 5L, 10L, 11L, 12L};

  public static List<RoutineTask> generateTestRoutineTasks(int count, List<Long> ids) {
    List<RoutineTask> routineTasks = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      RoutineTask routineTask = createRandomRoutineTask(ids.get(i));
      routineTasks.add(routineTask);
    }

    return routineTasks;
  }

  private static RoutineTask createRandomRoutineTask(Long id) {
    Long userId = USER_IDS[random.nextInt(USER_IDS.length)];
    String title = ROUTINE_TITLES[random.nextInt(ROUTINE_TITLES.length)] + " " + id;
    TaskType taskType = TaskType.values()[random.nextInt(TaskType.values().length)];
    Integer targetValue = taskType == TaskType.CHECK ? 1 : random.nextInt(100) + 1;

    AlarmOffsetType alarmType = random.nextBoolean() ?
        AlarmOffsetType.values()[random.nextInt(AlarmOffsetType.values().length)] : null;

    RoutineTimeInfo routineTimeInfo = createRandomRoutineTimeInfo();
    RecurrenceRule recurrenceRule = createRandomRecurrenceRule();

    OverrideInfo overrideInfo = null;
    if (random.nextDouble() < 0.1) {
      overrideInfo = OverrideInfo.builder()
          .overrideType(OverrideType.values()[random.nextInt(OverrideType.values().length)])
          .overrideDate(LocalDate.now().minusDays(random.nextInt(30)))
          .build();
    }

    return RoutineTask.builder()
        .userId(userId)
        .title(title)
        .taskType(taskType)
        .targetValue(targetValue)
        .routineTimeInfo(routineTimeInfo)
        .recurrenceRule(recurrenceRule)
        .alarmOffsetType(alarmType)
        .overrideInfo(overrideInfo)
        .build();
  }

  private static RoutineTimeInfo createRandomRoutineTimeInfo() {
    LocalDate startDate = LocalDate.now().minusDays(random.nextInt(90));
    LocalDate until = startDate.plusDays(30 + random.nextInt(60));
    LocalTime time = LocalTime.of(6 + random.nextInt(18), random.nextInt(4) * 15);

    Set<Weekday> weekdays;
    int pattern = random.nextInt(4);
    switch (pattern) {
      case 0: // 평일
        weekdays = Set.of(Weekday.MO, Weekday.TU, Weekday.WE, Weekday.TH, Weekday.FR);
        break;
      case 1: // 주말
        weekdays = Set.of(Weekday.SA, Weekday.SU);
        break;
      case 2: // 매일
        weekdays = EnumSet.allOf(Weekday.class);
        break;
      default: // 랜덤 요일들 (중복 제거)
        weekdays = generateRandomWeekdays();
    }

    return RoutineTimeInfo.builder()
        .startDate(startDate)
        .untilDate(until)
        .time(time)
        .weekdays(weekdays)
        .build();
  }

  private static Set<Weekday> generateRandomWeekdays() {
    Set<Weekday> weekdays = new HashSet<>();
    int count = random.nextInt(5) + 1;

    while (weekdays.size() < count) {
      weekdays.add(Weekday.values()[random.nextInt(7)]);
    }

    return weekdays;
  }

  private static RecurrenceRule createRandomRecurrenceRule() {
    Freq freq = Freq.values()[random.nextInt(Freq.values().length)];
    Integer interval = random.nextInt(3) + 1;

    Set<Weekday> byDay = null;
    if (freq == Freq.WEEKLY && random.nextBoolean()) {
      byDay = random.nextBoolean() ?
          Set.of(Weekday.MO, Weekday.WE, Weekday.FR) :
          Set.of(Weekday.SA, Weekday.SU);
    }

    LocalDateTime until = random.nextBoolean() ?
        LocalDateTime.now().plusDays(60 + random.nextInt(180)) : null;

    return RecurrenceRule.builder()
        .freq(freq)
        .interval(interval)
        .byDay(byDay)
        .until(until)
        .build();
  }
}
