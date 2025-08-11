package com.rouby.user.user.application.dto.command;

import com.rouby.user.user.application.service.verification.VerificationEmailCode;

public record SaveVerificationCodeCommand(
    String email,
    String code
) {

  public static SaveVerificationCodeCommand of(String email, String code) {
    return new SaveVerificationCodeCommand(email, code);
  }

  public VerificationEmailCode toEntity() {
    return VerificationEmailCode.of(code);
  }
}
