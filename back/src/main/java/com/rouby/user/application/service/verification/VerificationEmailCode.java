package com.rouby.user.application.service.verification;

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

  public VerificationEmailCode verified() {
    if(this.verified) throw new IllegalStateException("이미 인증되었습니다.");
    this.verified = true;
    return this;
  }
}
