package com.rouby.assistant.feedback.application.dto;

import java.util.List;

public record CreateFeedbackResult(
    String feedback,
    List<String> feedbackKeywords,
    List<String> userStatusKeywords
) {

  public CreateFeedbackResult {
    feedbackKeywords = feedbackKeywords == null ? List.of() : List.copyOf(feedbackKeywords);
    userStatusKeywords = userStatusKeywords == null ? List.of() : List.copyOf(userStatusKeywords);
  }
}
