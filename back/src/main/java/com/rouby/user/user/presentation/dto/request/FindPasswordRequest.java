package com.rouby.user.user.presentation.dto.request;

import com.rouby.user.user.application.dto.command.FindPasswordCommand;
import lombok.Builder;

@Builder
public record FindPasswordRequest(String email) {

  public FindPasswordCommand toCommand() {
    return FindPasswordCommand.builder()
        .email(email)
        .build();
  }
}
