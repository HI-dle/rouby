package com.rouby.schedule.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Disabled("필요한 경우 해당 어노테이션을 주석 처리하고 사용해주세요.")
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
    int maxSize = 1_000_000; // * 3 정도의 데이터 생성됨

    JdbcTestDataRepository repository = new JdbcTestDataRepository(jdbcTemplate, objectMapper);
    List<Schedule> schedules;
    for (int i = 0; i < maxSize; i += batchSize) {

      schedules = ScheduleTestDataFactory.generateTestSchedules(i, batchSize);
      repository.batchInsert(batchSize, schedules);
    }
  }
}
