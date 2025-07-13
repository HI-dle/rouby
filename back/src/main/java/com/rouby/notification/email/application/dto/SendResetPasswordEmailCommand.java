package com.rouby.notification.email.application.dto;


import com.rouby.notification.email.application.utils.ResetPasswordEmailData;

public record SendResetPasswordEmailCommand(
    String to,
    String type,
    ResetPasswordEmailData emailData
) implements SendEmailCommand {

  public static SendResetPasswordEmailCommand of(String to, String resetPasswordLink) {
    return new SendResetPasswordEmailCommand(to, "RESET_PASSWORD",
        ResetPasswordEmailData.of(resetPasswordLink));
  }
}
