package com.rouby.notification.email.application.utils;

import com.rouby.notification.email.application.dto.EmailData;

public record VerificationEmailData(
    String code
) implements EmailData {

  public static VerificationEmailData of(String code) {
    return new VerificationEmailData(code);
  }
}
