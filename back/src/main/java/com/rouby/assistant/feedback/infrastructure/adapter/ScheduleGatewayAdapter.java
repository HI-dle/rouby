package com.rouby.assistant.feedback.infrastructure.adapter;

import com.rouby.assistant.feedback.application.dto.SchedulesInfoForFeedback;
import com.rouby.assistant.feedback.application.port.outbound.ScheduleGateway;
import com.rouby.schedule.application.dto.query.GetScheduleQuery;
import com.rouby.schedule.application.service.ScheduleReadService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduleGatewayAdapter implements ScheduleGateway {
  private final ScheduleReadService scheduleReadService;

  @Override
  public SchedulesInfoForFeedback getSchedulesByCondition(
      Long userId, LocalDateTime fromAt, LocalDateTime toAt) {
    return SchedulesInfoForFeedback.from(scheduleReadService.findSchedulesBy(GetScheduleQuery.builder()
        .userId(userId)
        .fromAt(fromAt)
        .toAt(toAt)
        .build()));
  }
}
