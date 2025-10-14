package com.rouby.assistant.feedback.presentation.dto;

import com.rouby.assistant.feedback.application.dto.CreateFeedbackCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateFeedbackRequest(
    @NotBlank @Size(max = 1000)
    String userInput,
    @NotBlank @Pattern(regexp = "(?i)^(TERRIBLE|BAD|SOSO|GOOD|EXCELLENT)$")
    String userMood
) {

  public CreateFeedbackCommand toCommand(Long id) {
    return CreateFeedbackCommand.builder()
        .userId(id)
        .userInput(userInput)
        .userMood(userMood)
        .build();
  }
}
