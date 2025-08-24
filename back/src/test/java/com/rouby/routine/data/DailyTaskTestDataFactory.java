package com.rouby.routine.data;

import com.rouby.routine.daily_task.domain.DailyTask;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyTaskTestDataFactory {

  private static final Random random = new Random();

  public static List<DailyTask> generateTestDailyTasks(
      List<Long> routineTaskIds, int count) {

    List<DailyTask> dailyTasks = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      dailyTasks.add(createRandomDailyTask(routineTaskIds));
    }

    return dailyTasks;
  }

  private static DailyTask createRandomDailyTask(List<Long> routineTaskIds) {
    Long routineTaskId = routineTaskIds.get(random.nextInt(routineTaskIds.size()));
    LocalDate taskDate = LocalDate.now().minusDays(random.nextInt(60));
    Integer currentValue = generateRealisticProgress();

    return DailyTask.create(routineTaskId, taskDate, currentValue);
  }

  private static Integer generateRealisticProgress() {
    double rand = random.nextDouble();
    if (rand < 0.3) return 10;
    if (rand < 0.5) return random.nextInt(2);
    if (rand < 0.8) return 50 + random.nextInt(4);
    return random.nextInt(5);
  }
}