package com.rouby.user.presentation.dto.response;

import com.rouby.user.application.dto.info.LoginInfo;
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

