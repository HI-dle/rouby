package com.rouby.assistant.briefing.application.dto.info;

import com.rouby.assistant.briefing.domain.Briefing;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BriefingInfo(
    String content,
    LocalDateTime createdAt
) {

  public static BriefingInfo from(Briefing briefing) {
    return BriefingInfo.builder()
        .content(briefing.getBriefingContent().getContent())
        .createdAt(briefing.getCreatedAt())
        .build();
  }
}
