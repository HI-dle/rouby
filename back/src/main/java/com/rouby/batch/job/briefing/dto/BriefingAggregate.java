package com.rouby.batch.job.briefing.dto;

import com.rouby.assistant.briefing.application.dto.info.CreatedBriefingResult;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import java.util.List;

public record BriefingAggregate(
    CreatedBriefingResult briefing,
    List<NotificationEvent> notificationEvents
) {

}