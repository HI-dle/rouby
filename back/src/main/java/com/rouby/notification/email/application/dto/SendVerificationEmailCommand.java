package com.rouby.notification.email.application.dto;


import com.rouby.notification.email.application.utils.VerificationEmailData;

public record SendVerificationEmailCommand(
    String to,
    String type,
    VerificationEmailData emailData
) implements SendEmailCommand {

  public static SendVerificationEmailCommand of(String to, String code) {
    return new SendVerificationEmailCommand(to, "VERIFICATION", VerificationEmailData.of(code));
  }
}
