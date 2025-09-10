package com.rouby.assistant.briefing.application.dto.info;

import com.rouby.assistant.briefing.domain.Briefing;
import lombok.Builder;

@Builder
public record CreatedBriefingResult(
    Long userId,
    String prompt,
    Long promptTemplateId,
    String content
) {

  public static CreatedBriefingResult of(Long userId, String prompt, Long promptTemplateId,
      String content) {
    return CreatedBriefingResult.builder()
        .userId(userId)
        .prompt(prompt)
        .promptTemplateId(promptTemplateId)
        .content(content)
        .build();
  }

  public Briefing toEntity() {
    return Briefing.builder()
        .userId(userId)
        .prompt(prompt)
        .promptTemplateId(promptTemplateId)
        .content(content)
        .build();
  }
}
