package com.rouby.user.presentation.dto;

import com.rouby.user.application.SendEmailCommand;
import jakarta.validation.constraints.Email;

public record SendEmailVerificationRequest(
    @Email(message = "잘못된 이메일 양식입니다.")
    String email
) {

  public SendEmailCommand toCommand(String to, String subject, String text) {
    return new SendEmailCommand(to, subject, text);
  }
}
