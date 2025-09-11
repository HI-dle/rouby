package com.rouby.assistant.briefing.presentation.dto.response;

import com.rouby.assistant.briefing.application.dto.info.BriefingInfo;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BriefingResponse(
    String content,
    LocalDateTime createdAt
) {

  public static BriefingResponse of(BriefingInfo briefingInfo) {
    return BriefingResponse.builder()
        .content(briefingInfo.content())
        .createdAt(briefingInfo.createdAt())
        .build();
  }
}
