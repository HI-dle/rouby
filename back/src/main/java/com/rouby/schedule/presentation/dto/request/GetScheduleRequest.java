package com.rouby.schedule.presentation.dto.request;

import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record GetScheduleRequest(
    @NotNull LocalDateTime fromAt,
    @NotNull LocalDateTime toAt
) {

  public GetScheduleRequest {
    if (!fromAt.isBefore(toAt)) {
      throw new IllegalArgumentException("일정 조회 시작일시가 종료일시보다 나중일 수 없습니다.");
    }
  }

  public GetScheduleQuery toQuery(Long userId) {
    return GetScheduleQuery.builder()
        .userId(userId)
        .fromAt(fromAt)
        .toAt(toAt)
        .build();
  }
}
