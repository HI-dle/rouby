package com.rouby.schedule.fixture;

import com.rouby.schedule.presentation.dto.request.GetScheduleRequest;
import java.time.LocalDate;

public class GetScheduleRequestFixture {

  public static GetScheduleRequest getSuccessRequest() {
    return GetScheduleRequest.builder()
        .fromAt(LocalDate.now().plusDays(14).atStartOfDay())
        .toAt(LocalDate.now().plusDays(15).atStartOfDay())
        .build();
  }

}
