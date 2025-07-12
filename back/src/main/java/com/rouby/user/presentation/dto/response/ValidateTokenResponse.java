package com.rouby.user.presentation.dto.response;

import com.rouby.user.application.dto.response.ValidateTokenInfo;
import lombok.Builder;

@Builder
public record ValidateTokenResponse(Long userId) {

  public static ValidateTokenResponse from(ValidateTokenInfo info) {
    return ValidateTokenResponse.builder()
        .userId(info.userId())
        .build();
  }
}
