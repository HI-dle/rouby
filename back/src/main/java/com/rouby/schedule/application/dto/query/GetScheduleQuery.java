package com.rouby.schedule.application.dto.query;

import com.rouby.schedule.domain.repository.criteria.GetScheduleCriteria;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetScheduleQuery(
    Long userId,
    LocalDateTime fromAt,
    LocalDateTime toAt
) {

  public GetScheduleCriteria toCriteria() {
    return GetScheduleCriteria.builder()
        .userId(userId)
        .fromAt(fromAt)
        .toAt(toAt)
        .build();
  }
}
