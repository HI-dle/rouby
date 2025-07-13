package com.rouby.user.application.dto.command;

import com.rouby.notification.email.application.dto.SendResetPasswordEmailCommand;
import lombok.Builder;

@Builder
public record FindPasswordCommand(String email) {

  public SendResetPasswordEmailCommand toSendEmailCommand(String to, String resetPasswordLink) {
    return SendResetPasswordEmailCommand.of(to, resetPasswordLink);
  }
}
