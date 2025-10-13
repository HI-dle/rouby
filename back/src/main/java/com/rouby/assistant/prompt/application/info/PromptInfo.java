package com.rouby.assistant.prompt.application.info;

import com.rouby.assistant.prompt.domain.entity.Prompt;
import lombok.Builder;

@Builder
public record PromptInfo(
    Long id,
    String promptType,
    String systemMessage,
    String userMessage,
    Integer version
) {
  public static PromptInfo from(Prompt prompt){
    return PromptInfo.builder()
        .id(prompt.getId())
        .promptType(prompt.getPromptType().toString())
        .systemMessage(prompt.getSystemMessage())
        .userMessage(prompt.getUserMessage())
        .version(prompt.getVersion())
        .build();
  }
}
