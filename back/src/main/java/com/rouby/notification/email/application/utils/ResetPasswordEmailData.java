package com.rouby.notification.email.application.utils;

import com.rouby.notification.email.application.dto.EmailData;

public record ResetPasswordEmailData(
    String resetPasswordLink
) implements EmailData {

  public static ResetPasswordEmailData of(String resetPasswordLink) {
    return new ResetPasswordEmailData(resetPasswordLink);
  }
}
