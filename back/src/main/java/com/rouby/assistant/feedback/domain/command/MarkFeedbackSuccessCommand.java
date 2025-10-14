package com.rouby.assistant.feedback.domain.command;

import com.rouby.assistant.feedback.application.dto.CreateFeedbackResult;
import java.util.List;
import lombok.Builder;

@Builder
public record MarkFeedbackSuccessCommand(
    Long feedbackId,
    String feedback,
    List<String> feedbackKeywords,
    List<String> userStatusKeywords
) {

  public static MarkFeedbackSuccessCommand from(Long feedbackId, CreateFeedbackResult result) {

    return MarkFeedbackSuccessCommand.builder()
        .feedbackId(feedbackId)
        .feedback(result.feedback())
        .feedbackKeywords(result.feedbackKeywords())
        .userStatusKeywords(result.userStatusKeywords())
        .build();
  }
}
