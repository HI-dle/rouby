package com.rouby.assistant.prompt.application.command;

import com.rouby.assistant.prompt.domain.entity.Prompt;
import com.rouby.assistant.prompt.domain.entity.enums.PromptType;
import lombok.Builder;

@Builder
public record CreatePromptCommand(
    PromptType promptType,
    String systemMessage,
    String userMessage) {

  public Prompt toEntity(int version){

    return Prompt.builder()
        .promptType(promptType)
        .systemMessage(systemMessage)
        .userMessage(userMessage)
        .version(version)
        .build();
  }
}
