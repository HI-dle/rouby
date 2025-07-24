package com.rouby.schedule.data;

import com.rouby.schedule.domain.entity.Schedule;
import com.rouby.schedule.domain.repository.ScheduleRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("필요한 경우 해당 어노테이션을 주석 처리하고 사용해주세요.")
@SpringBootTest
public class ScheduleTestDataSaver {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Test
  @DisplayName("스케쥴 기본 데이터 생성")
  void createSchedules() {

    List<Schedule> schedules = ScheduleTestDataFactory.generateTestSchedules(100);
    scheduleRepository.saveAll(schedules);
  }
}
