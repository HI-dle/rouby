package com.rouby.batch.job.briefing.dto;

import com.rouby.assistant.briefing.application.dto.info.CreatedBriefingResult;
import java.util.Objects;

public record BriefingAggregate(
    CreatedBriefingResult briefing,
    BriefingNotificationEvents notificationEvents
) {

    public BriefingAggregate {
        Objects.requireNonNull(briefing, "briefing must not be null");
        Objects.requireNonNull(notificationEvents, "notificationEvents must not be null");
    }
}