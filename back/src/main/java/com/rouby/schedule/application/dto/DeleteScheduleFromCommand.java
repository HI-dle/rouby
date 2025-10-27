package com.rouby.schedule.application.dto;

import java.time.LocalDateTime;

public record DeleteScheduleFromCommand(
    Long scheduleId,
    LocalDateTime fromAt
) {

}
