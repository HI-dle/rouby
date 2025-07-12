package com.rouby.notification.email.application.dto;


import com.rouby.notification.email.application.utils.VerificationEmailData;

public record SendVerificationEmailCommand(
    String to,
    String subject,
    String type,
    VerificationEmailData emailData
) implements SendEmailCommand {

  public static SendVerificationEmailCommand of(String to, String subject, String code) {
    return new SendVerificationEmailCommand(to, subject, "VERIFICATION", VerificationEmailData.of(code));
  }
}
