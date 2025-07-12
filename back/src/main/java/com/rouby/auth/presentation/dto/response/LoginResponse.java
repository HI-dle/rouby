package com.rouby.auth.presentation.dto.response;

import com.rouby.auth.application.dto.response.LoginInfo;
import lombok.Builder;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Builder
public record LoginResponse(String token) {

  public static LoginResponse from(LoginInfo info){
    return LoginResponse.builder()
        .token(info.accessToken())
        .build();
  }
}

