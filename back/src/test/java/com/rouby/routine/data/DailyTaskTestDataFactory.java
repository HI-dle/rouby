package com.rouby.routine.data;

import com.rouby.routine.daily_task.domain.DailyTask;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyTaskTestDataFactory {

  private static final Random random = new Random();

  public static List<DailyTask> generateTestDailyTasks(
      List<Long> routineTaskIds, int count, List<Long> ids) {

    List<DailyTask> dailyTasks = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      dailyTasks.add(createRandomDailyTask(routineTaskIds, ids.get(i)));
    }

    return dailyTasks;
  }

  private static DailyTask createRandomDailyTask(List<Long> routineTaskIds, Long id) {
    Long routineTaskId = routineTaskIds.get(random.nextInt(routineTaskIds.size()));
    LocalDate taskDate = LocalDate.now().minusDays(random.nextInt(60));
    Integer currentValue = generateRealisticProgress();

    return DailyTask.create(routineTaskId, taskDate, currentValue);
  }

  private static Integer generateRealisticProgress() {
    // 현실적인 진행률 패턴
    double rand = random.nextDouble();
    if (rand < 0.3) return 100; // 30% 완료
    if (rand < 0.5) return random.nextInt(20); // 20% 거의 안함
    if (rand < 0.8) return 50 + random.nextInt(40); // 50% 중간 정도
    return random.nextInt(50); // 20% 낮은 진행률
  }
}