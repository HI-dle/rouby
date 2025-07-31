package com.rouby.schedule.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.Pair;

//@Disabled("필요한 경우 해당 어노테이션을 주석 처리하고 사용해주세요.")
@SpringBootTest
public class ScheduleTestDataSaver {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("스케쥴 기본 데이터 생성")
  void createSchedules() {

    int batchSize = 100;
    int maxSize = 1_000; // * 3 정도의 데이터 생성됨

    JdbcTestDataRepository repository = new JdbcTestDataRepository(jdbcTemplate, objectMapper);
    List<Schedule> schedules;
    int totalSize = ScheduleTestDataFactory.getCountSchedules(maxSize);

    List<Long> ids = repository.fetchNextIds("schedule_id_seq", totalSize);

    int idIndex = 0;

    for (int i = 0; i < maxSize; i += batchSize) {
      try {
        schedules = ScheduleTestDataFactory.generateTestSchedules(i, batchSize, ids, idIndex);
        repository.batchInsert(batchSize, schedules);
        idIndex += ScheduleTestDataFactory.getCountSchedules(Math.min(batchSize, maxSize - i));

        System.out.printf("Inserted batch %d-%d%n", i, i + batchSize);

      } catch (Exception e) {

        System.err.printf("Failed to insert batch %d-%d: %s%n", i, i + batchSize, e.getMessage());
        break;
      }
    }
  }
}
