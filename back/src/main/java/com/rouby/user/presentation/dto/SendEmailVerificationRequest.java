package com.rouby.user.presentation.dto;

import com.rouby.user.application.dto.SendEmailVerificationCommand;
import jakarta.validation.constraints.Email;

public record SendEmailVerificationRequest(
    @Email(message = "잘못된 이메일 양식입니다.")
    String email
) {

  public SendEmailVerificationCommand toCommand() {
    return new SendEmailVerificationCommand(email);
  }
}
