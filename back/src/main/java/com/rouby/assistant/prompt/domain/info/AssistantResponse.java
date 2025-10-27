package com.rouby.assistant.prompt.domain.info;

import lombok.Builder;

@Builder
public record AssistantResponse<R>(
    R result
) {

  public static <R> AssistantResponse<R> of(R result) {
    return new AssistantResponse<>(result);
  }
}
