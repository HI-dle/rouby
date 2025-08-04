package com.rouby.assistant.prompt.application.info;

import com.rouby.assistant.prompt.domain.Prompt;
import com.rouby.assistant.prompt.domain.enums.PromptType;

public record PromptInfo(
    PromptType promptType,
    String promptTemplate,
    Integer version
) {
  public static PromptInfo of(Prompt prompt){
    return new PromptInfo(prompt.getPromptType(),
        prompt.getPromptTemplate(),
        prompt.getVersion());
  }
}
