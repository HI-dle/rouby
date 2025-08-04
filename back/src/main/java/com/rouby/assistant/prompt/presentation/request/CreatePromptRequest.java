package com.rouby.assistant.prompt.presentation.request;

import com.rouby.assistant.prompt.application.command.CreatePromptCommand;
import com.rouby.assistant.prompt.domain.enums.PromptType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePromptRequest(
  @NotNull(message = "프롬프트 타입은 필수입니다")
  PromptType promptType,
  @NotBlank(message = "프롬프트 템플릿은 필수입니다")
  String promptTemplate,
  @NotNull(message = "버전은 필수입니다")
  @Positive(message = "버전은 양수여야 합니다")
  Integer version
) {
  public CreatePromptCommand toCommand() {
    return new CreatePromptCommand(promptType, promptTemplate, version);
  }
}
