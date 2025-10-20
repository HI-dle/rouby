package com.rouby.assistant.feedback.presentation.dto;

import com.rouby.assistant.feedback.application.dto.GetDailyFeedbacksInfo;
import com.rouby.assistant.feedback.application.dto.GetDailyFeedbacksInfo.GetFeedbackInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record GetDailyFeedbacksResponse(
    List<GetDailyFeedbackResponse> feedbacks
) {

  public static GetDailyFeedbacksResponse from(GetDailyFeedbacksInfo feedbacksInfo) {
    return new GetDailyFeedbacksResponse(
        feedbacksInfo.feedbacks().stream().map(GetDailyFeedbackResponse::from).toList());
  }

  @Builder
  record GetDailyFeedbackResponse(
      int slot,
      String userMood,
      String userInput,
      String feedbackContent,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
  ) {

    public static GetDailyFeedbackResponse from(GetFeedbackInfo info) {

      return GetDailyFeedbackResponse.builder()
          .slot(info.slot())
          .userMood(info.userMood())
          .userInput(info.userInput())
          .feedbackContent(info.feedbackContent())
          .createdAt(info.createdAt())
          .updatedAt(info.updatedAt())
          .build();
    }
  }
}
