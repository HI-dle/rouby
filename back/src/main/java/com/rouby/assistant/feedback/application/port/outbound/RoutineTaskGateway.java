package com.rouby.assistant.feedback.application.port.outbound;

import com.rouby.assistant.feedback.application.dto.RoutineTasksInfoForFeedback;
import java.time.LocalDate;

public interface RoutineTaskGateway {

  RoutineTasksInfoForFeedback getRoutineTaskWithProgress(Long userId, LocalDate fromDate, LocalDate toDate);
}
