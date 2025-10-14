package com.rouby.assistant.feedback.application.dto;

import com.rouby.assistant.feedback.domain.entity.Feedback;
import com.rouby.assistant.feedback.domain.entity.enums.Mood;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CreateFeedbackCommand(
    Long userId,
    String userInput,
    String userMood
) {

  public Feedback toEntity(LocalDate today, Integer slot, Integer version) {
    return Feedback.builder()
        .userId(userId)
        .feedbackDate(today)
        .mood(Mood.parse(userMood))
        .userInput(userInput)
        .slot(slot)
        .promptTemplateVersion(version)
        .build();
  }
}
