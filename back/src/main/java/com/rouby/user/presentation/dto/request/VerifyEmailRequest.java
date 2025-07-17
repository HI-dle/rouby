package com.rouby.user.presentation.dto.request;

import com.rouby.user.application.dto.command.VerifyEmailCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record VerifyEmailRequest(
    @Email(message = "이메일 형식이 아닙니다.")
    String email,

    @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
    String code
) {

  public VerifyEmailCommand toCommand() {
    return VerifyEmailCommand.of(email, code);
  }
}
