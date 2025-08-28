package com.rouby.routine.data;

import com.rouby.routine.daily_task.domain.DailyTask;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DailyTaskTestDataFactory {

  public static List<DailyTask> generateTestDailyTasks(List<Long> routineTaskIds, int count) {

    List<DailyTask> dailyTasks = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      dailyTasks.add(createRandomDailyTask(routineTaskIds));
    }

    return dailyTasks;
  }

  private static DailyTask createRandomDailyTask(List<Long> routineTaskIds) {
    ThreadLocalRandom tlr = ThreadLocalRandom.current();
    Long routineTaskId = routineTaskIds.get(tlr.nextInt(routineTaskIds.size()));
    LocalDate taskDate = LocalDate.now().minusDays(tlr.nextInt(150));
    Integer currentValue = generateRealisticProgress();

    return DailyTask.create(routineTaskId, taskDate, currentValue);
  }

  private static Integer generateRealisticProgress() {
    ThreadLocalRandom tlr = ThreadLocalRandom.current();
    double rand = tlr.nextDouble();
    if (rand < 0.3) return 10;
    if (rand < 0.5) return tlr.nextInt(2);
    if (rand < 0.8) return 50 + tlr.nextInt(4);
    return tlr.nextInt(5);
  }
}