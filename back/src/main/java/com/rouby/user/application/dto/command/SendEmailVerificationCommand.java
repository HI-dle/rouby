package com.rouby.user.application.dto.command;

import com.rouby.notification.email.application.dto.SendVerificationEmailCommand;

public record SendEmailVerificationCommand(
    String email
) {

  public SendVerificationEmailCommand toSendEmailCommand(String to, String code) {
    return SendVerificationEmailCommand.of(to, code);
  }
}
