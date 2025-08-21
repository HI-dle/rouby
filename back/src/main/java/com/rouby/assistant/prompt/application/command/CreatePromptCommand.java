package com.rouby.assistant.prompt.application.command;

import com.rouby.assistant.prompt.domain.Prompt;
import com.rouby.assistant.prompt.domain.enums.PromptType;

public record CreatePromptCommand(
    PromptType promptType,
    String promptTemplate,
    Integer version) {

  public Prompt toEntity(){
    return Prompt.builder()
        .promptType(promptType)
        .promptTemplate(promptTemplate)
        .version(version)
        .build();
  }
}
