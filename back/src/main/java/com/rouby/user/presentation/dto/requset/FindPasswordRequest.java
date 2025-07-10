package com.rouby.user.presentation.dto.requset;

import com.rouby.user.application.dto.command.FindPasswordCommand;
import lombok.Builder;

@Builder
public record FindPasswordRequest(String email) {

  public FindPasswordCommand toCommand() {
    return FindPasswordCommand.builder()
        .email(email)
        .build();
  }
}
