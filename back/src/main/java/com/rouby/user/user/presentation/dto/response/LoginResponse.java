package com.rouby.user.user.presentation.dto.response;

import com.rouby.user.user.application.dto.info.LoginInfo;
import lombok.Builder;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Builder
public record LoginResponse(
    String accessToken,
    String refreshToken
) {

  public static LoginResponse from(LoginInfo info){
    return LoginResponse.builder()
        .accessToken(info.accessToken())
        .refreshToken(info.refreshToken())
        .build();
  }
}

