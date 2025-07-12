package com.rouby.user.application.dto;

import com.rouby.notification.email.application.dto.SendVerificationEmailCommand;

public record SendEmailVerificationCommand(
    String email
) {

  public SendVerificationEmailCommand toSendEmailCommand(String to, String subject, String code) {
    return SendVerificationEmailCommand.of(to, subject, code);
  }
}
