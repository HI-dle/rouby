package com.rouby.assistant.prompt.presentation.request;

import com.rouby.assistant.prompt.application.command.CreatePromptCommand;
import com.rouby.assistant.prompt.domain.enums.PromptType;

public record CreatePromptRequest(
  PromptType promptType,
  String promptTemplate,
  Integer version
) {
  public CreatePromptCommand toCommand() {
    return new CreatePromptCommand(promptType, promptTemplate, version);
  }
}
