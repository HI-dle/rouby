package com.rouby.schedule.domain.repository.criteria;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetScheduleCriteria(
    Long userId,
    LocalDateTime fromAt,
    LocalDateTime toAt
) {
}
