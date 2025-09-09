package com.rouby.batch.job.briefing.dto;

import com.rouby.assistant.briefing.application.dto.info.CreatedBriefingResult;

public record BriefingAggregate(
    CreatedBriefingResult briefing,
    BriefingNotificationEvents notificationEvents
) {

}