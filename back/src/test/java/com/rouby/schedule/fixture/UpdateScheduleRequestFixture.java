package com.rouby.schedule.fixture;

import com.rouby.schedule.presentation.dto.request.UpdateScheduleRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UpdateScheduleRequestFixture {

  public static UpdateScheduleRequest getSuccessRequest() {
    return new UpdateScheduleRequest(
        1L,
        2L,
        "수정된 일정 제목",
        "수정된 메모",
        30,
        LocalDate.of(2025, 9, 15),
        LocalDateTime.of(2025, 9, 15, 10, 0),
        LocalDateTime.of(2025, 9, 15, 12, 0)
    );
  }

  public static UpdateScheduleRequest getAlarmOffsetFailedRequest() {
    return new UpdateScheduleRequest(
    1L,
        2L,
        "수정된 일정 제목",
        "수정된 메모",
        1340,
        LocalDate.of(2025, 9, 15),
        LocalDateTime.of(2025, 9, 15, 10, 0),
        LocalDateTime.of(2025, 9, 15, 12, 0)
    );
  }
}
