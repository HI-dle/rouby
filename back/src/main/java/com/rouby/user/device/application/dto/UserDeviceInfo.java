package com.rouby.user.device.application.dto;

import com.rouby.user.device.domain.entity.UserDevice;
import lombok.Builder;

@Builder
public record UserDeviceInfo(
    Long userId,
    String deviceType,
    String deviceToken,
    String tokenProvider
) {

  public static UserDeviceInfo from(UserDevice userDevice) {
    return UserDeviceInfo.builder()
        .userId(userDevice.getUserId())
        .deviceType(userDevice.getDeviceInfo().getDeviceType().toString())
        .deviceToken(userDevice.getTokenInfo().getDeviceToken())
        .tokenProvider(userDevice.getTokenInfo().getTokenProvider().toString())
        .build();
  }
}
