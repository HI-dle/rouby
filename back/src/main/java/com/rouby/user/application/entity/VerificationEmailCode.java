package com.rouby.user.application.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationEmailCode {
  private String code;
  private boolean verified;

  public static VerificationEmailCode of(String code) {
    return new VerificationEmailCode(code);
  }

  private VerificationEmailCode(String code) {
    this.code = code;
    verified = false;
  }

  public void verified() {
    this.verified = true;
  }
}
