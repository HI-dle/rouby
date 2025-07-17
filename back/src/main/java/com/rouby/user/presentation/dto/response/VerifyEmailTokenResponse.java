package com.rouby.user.presentation.dto.response;

public record VerifyEmailTokenResponse(
    String token
) {
  public static VerifyEmailTokenResponse of(String token) {
    return new VerifyEmailTokenResponse(token);
  }
}
