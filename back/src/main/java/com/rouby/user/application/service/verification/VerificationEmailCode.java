package com.rouby.user.application.service.verification;

import static com.rouby.user.application.exception.UserErrorCode.EMAIL_ALREADY_VERIFIED;

import com.rouby.user.application.exception.UserException;
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
    if(this.verified) throw UserException.from(EMAIL_ALREADY_VERIFIED);
    this.verified = true;
    return this;
  }
}
