package com.rouby.user.presentation.dto.request;

import com.rouby.user.application.dto.command.SendEmailVerificationCommand;
import jakarta.validation.constraints.Email;

public record SendEmailVerificationRequest(
    @Email(message = "잘못된 이메일 양식입니다.")
    String email
) {

  public SendEmailVerificationCommand toCommand() {
    return new SendEmailVerificationCommand(email);
  }
}
