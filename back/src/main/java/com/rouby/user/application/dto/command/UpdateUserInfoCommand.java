package com.rouby.user.application.dto.command;

import java.time.LocalTime;
import java.util.Set;
import lombok.Builder;

@Builder
public record UpdateUserInfoCommand(
    Long updaterId,
    String nickname,
    Set<String> healthStatusKeywords,
    Set<String> profileKeywords,
    LocalTime dailyStartTime,
    LocalTime dailyEndTime
) {

}
