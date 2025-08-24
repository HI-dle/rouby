package com.rouby.routine.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.routine.daily_task.domain.DailyTask;
import com.rouby.routine.daily_task.domain.repository.DailyTaskRepository;
import com.rouby.routine.routine_task.domain.RoutineTask;
import com.rouby.routine.routine_task.domain.repository.RoutineTaskRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Disabled("필요한 경우 해당 어노테이션을 주석 처리하고 사용해주세요.")
@SpringBootTest
public class RoutineTaskTestDataSaver {

  @Autowired
  private RoutineTaskRepository routineTaskRepository;

  @Autowired
  private DailyTaskRepository dailyTaskRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("루틴 태스크 기본 데이터 생성")
  void createRoutineTasks() {
    int batchSize = 500;
    int maxSize = 100_000;

    RoutineTaskJdbcRepository repository = new RoutineTaskJdbcRepository(jdbcTemplate, objectMapper);
    List<RoutineTask> routineTasks;

    List<Long> ids = repository.fetchNextRoutineTaskIds(maxSize);

    for (int i = 0; i < maxSize; i += batchSize) {
      try {
        int currentBatchSize = Math.min(batchSize, maxSize - i);
        routineTasks = RoutineTaskTestDataFactory.generateTestRoutineTasks(
            i, currentBatchSize, ids.subList(i, i + currentBatchSize));

        repository.batchInsertRoutineTasks(routineTasks, ids.subList(i, i + currentBatchSize));

        System.out.printf("Inserted routine task batch %d-%d%n", i, i + currentBatchSize);

      } catch (Exception e) {
        System.err.printf("Failed to insert routine task batch %d-%d: %s%n",
            i, i + batchSize, e.getMessage());
        e.printStackTrace();
        break;
      }
    }
  }

  @Test
  @DisplayName("데일리 태스크 대용량 데이터 생성 (100만건)")
  void createLargeDailyTasks() {
    int batchSize = 500;
    int maxSize = 1_000_000;

    List<Long> routineTaskIds = jdbcTemplate.queryForList(
        "SELECT id FROM routine_tasks WHERE deleted_at IS NULL LIMIT 100", Long.class);

    if (routineTaskIds.isEmpty()) {
      System.out.println("루틴 태스크가 없습니다.");
      return;
    }

    DailyTaskJdbcRepository repository = new DailyTaskJdbcRepository(jdbcTemplate);

    for (int i = 0; i < maxSize; i += batchSize) {
      try {
        int currentBatchSize = Math.min(batchSize, maxSize - i);

        List<DailyTask> dailyTasks = DailyTaskTestDataFactory
            .generateTestDailyTasks(routineTaskIds, currentBatchSize);

        repository.batchInsertDailyTasks(dailyTasks);

        if (i % 10000 == 0) {
          System.out.printf("Progress: %d/%d (%.1f%%)%n",
              i, maxSize, (double)i/maxSize*100);
        }

      } catch (Exception e) {
        System.err.printf("Failed at batch %d: %s%n", i, e.getMessage());
        break;
      }
    }

    System.out.println("대용량 데이터 생성 완료!");
  }
}