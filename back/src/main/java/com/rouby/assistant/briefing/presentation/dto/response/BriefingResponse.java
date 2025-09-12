package com.rouby.assistant.briefing.presentation.dto.response;

import com.rouby.assistant.briefing.application.dto.info.BriefingInfo;
import java.time.LocalDateTime;

public record BriefingResponse(
    String content,
    LocalDateTime createdAt
) {

  public static BriefingResponse of(BriefingInfo briefingInfo) {
    return new BriefingResponse(briefingInfo.content(), briefingInfo.createdAt());
  }
}
