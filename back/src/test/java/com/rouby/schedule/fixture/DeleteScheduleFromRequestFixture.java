package com.rouby.schedule.fixture;

import com.rouby.schedule.presentation.dto.DeleteScheduleFromRequest;
import java.time.LocalDateTime;

public class DeleteScheduleFromRequestFixture {

  public static DeleteScheduleFromRequest getSuccessRequest() {
    return new DeleteScheduleFromRequest(
        1L,
        LocalDateTime.of(2025, 9, 15, 10, 0)
    );
  }

  public static DeleteScheduleFromRequest getPastDateRequest() {
    return new DeleteScheduleFromRequest(
        1L,
        LocalDateTime.of(2020, 1, 1, 10, 0) // 과거 날짜 예시
    );
  }
}