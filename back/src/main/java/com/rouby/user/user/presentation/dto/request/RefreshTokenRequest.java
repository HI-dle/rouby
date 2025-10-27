package com.rouby.user.user.presentation.dto.request;

import com.rouby.user.user.application.dto.command.RefreshTokenCommand;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "토큰 정보는 필수입니다.")
    String refreshToken
) {

  public RefreshTokenCommand toApplication() {
    return new RefreshTokenCommand(refreshToken);
  }
}
