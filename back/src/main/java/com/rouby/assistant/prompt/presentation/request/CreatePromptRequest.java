package com.rouby.assistant.prompt.presentation.request;

import com.rouby.assistant.prompt.application.command.CreatePromptCommand;
import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreatePromptRequest(
  @NotNull(message = "프롬프트 타입은 필수입니다")
  PromptType promptType,
  String systemMessage,
  @NotBlank(message = "프롬프트 템플릿의 유저 메세지(본문)는 필수입니다")
  String userMessage
) {
  public CreatePromptCommand toCommand() {
    return CreatePromptCommand.builder()
        .promptType(promptType)
        .systemMessage(systemMessage)
        .userMessage(userMessage)
        .build();
  }
}
