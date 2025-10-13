package com.rouby.assistant.feedback.application.dto;

import java.util.List;

public record CreateFeedbackResult(
    String feedback,
    List<String> feedbackKeywords,
    List<String> userStatusKeywords
) {
}
