package com.rouby.user.user.presentation.dto.response;

import com.rouby.user.user.application.dto.info.TokenInfo;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {

  public static TokenResponse from(TokenInfo tokenInfo) {
    return new TokenResponse(tokenInfo.accessToken(), tokenInfo.refreshToken());
  }
}
