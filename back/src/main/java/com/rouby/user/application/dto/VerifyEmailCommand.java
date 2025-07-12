package com.rouby.user.application.dto;

public record VerifyEmailCommand(
    String email,
    String code
) {

  public static VerifyEmailCommand of(String email, String code) {
    return new VerifyEmailCommand(email, code);
  }
}
