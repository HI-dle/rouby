package com.rouby.user.application.dto;

public record SendEmailVerificationCommand(
    String email
) {
  public SendEmailCommand toSendEmailCommand(String to, String subject, String text) {
    return new SendEmailCommand(to, subject, text);
  }
}
