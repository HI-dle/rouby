package com.rouby.schedule.fixture;

import com.rouby.schedule.presentation.dto.DeleteScheduleRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DeleteScheduleRequestFixture {

  public static DeleteScheduleRequest getSuccessRequest() {
    return new DeleteScheduleRequest(
        1L,
        LocalDate.of(2025, 9, 15),
        LocalDateTime.of(2025, 9, 15, 10, 0),
        LocalDateTime.of(2025, 9, 15, 12, 0)
    );
  }

  public static DeleteScheduleRequest getInvalidDateRequest() {
    return new DeleteScheduleRequest(
        1L,
        LocalDate.of(2025, 9, 15),
        LocalDateTime.of(2025, 9, 15, 12, 0),
        LocalDateTime.of(2025, 9, 15, 10, 0)
    );
  }
}