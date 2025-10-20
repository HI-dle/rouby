package com.rouby.assistant.feedback.application.dto;

import com.rouby.assistant.feedback.domain.entity.Feedback;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public record GetDailyFeedbacksInfo(
    List<GetFeedbackInfo> feedbacks
) {

  public static GetDailyFeedbacksInfo from(List<Feedback> feedbacks) {

    if (feedbacks == null || feedbacks.isEmpty()) {
      return new GetDailyFeedbacksInfo(Collections.emptyList());
    }

    return GetDailyFeedbacksInfo.builder()
        .feedbacks(feedbacks.stream().map(GetFeedbackInfo::from).toList())
        .build();
  }

  @Builder
  public record GetFeedbackInfo(
      int slot,
      String userInput,
      String userMood,
      String feedbackContent,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
  ) {

    public static GetFeedbackInfo from(Feedback feedback) {

      return GetFeedbackInfo.builder()
          .slot(feedback.getSlot())
          .userInput(feedback.getUserInput())
          .userMood(feedback.getMood().toString())
          .feedbackContent(feedback.getFeedbackContent().getContent())
          .status(feedback.getStatus().toString())
          .createdAt(feedback.getCreatedAt())
          .updatedAt(feedback.getUpdatedAt())
          .build();
    }
  }
}
