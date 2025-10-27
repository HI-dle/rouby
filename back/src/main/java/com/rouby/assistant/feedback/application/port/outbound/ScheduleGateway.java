package com.rouby.assistant.feedback.application.port.outbound;

import com.rouby.assistant.feedback.application.dto.SchedulesInfoForFeedback;
import java.time.LocalDateTime;

public interface ScheduleGateway {

  SchedulesInfoForFeedback getSchedulesByCondition(Long userId, LocalDateTime fromAt, LocalDateTime toAt);
}
