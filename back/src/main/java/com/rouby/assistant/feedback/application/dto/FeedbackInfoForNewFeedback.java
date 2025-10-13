package com.rouby.assistant.feedback.application.dto;

import com.rouby.assistant.feedback.domain.entity.Feedback;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public record FeedbackInfoForNewFeedback(
    List<String> feedbackKeyword,
    List<String> statusKeyword
) {

  public static FeedbackInfoForNewFeedback from(Feedback recentFeedback) {

    if  (recentFeedback == null) return new FeedbackInfoForNewFeedback(Collections.emptyList(), Collections.emptyList());

    return FeedbackInfoForNewFeedback.builder()
        .feedbackKeyword(recentFeedback.getFeedbackKeyword().getFeedbackKeyword())
        .statusKeyword(recentFeedback.getUserStatusKeyword().getStatusKeyword())
        .build();
  }
}
