package com.rouby.user.device.application.dto.command;

import com.rouby.user.device.domain.entity.enums.AppType;
import com.rouby.user.device.domain.entity.enums.DeviceType;
import com.rouby.user.device.domain.entity.enums.TokenProviderType;
import com.rouby.user.device.domain.entity.vo.DeviceInfo;
import com.rouby.user.device.domain.entity.vo.DeviceTokenInfo;
import com.rouby.user.device.domain.entity.UserDevice;
import lombok.Builder;

@Builder
public record RegisterUserDeviceCommand(
    Long userId,
    String deviceToken,
    String tokenProvider,
    String appType,
    String appVersion,
    String deviceType,
    String os,
    String browser,
    String userAgent
) {

  public UserDevice toEntity() {

    return UserDevice.builder()
        .userId(userId)
        .tokenInfo(buildDeviceTokenInfo())
        .deviceInfo(buildDeviceInfo())
        .build();
  }

  public DeviceInfo buildDeviceInfo() {
    return DeviceInfo.builder()
        .appType(AppType.parse(appType))
        .appVersion(appVersion)
        .deviceType(DeviceType.parse(deviceType))
        .os(os)
        .browser(browser)
        .userAgent(userAgent)
        .build();
  }

  public DeviceTokenInfo buildDeviceTokenInfo() {
    return DeviceTokenInfo.builder()
        .deviceToken(deviceToken)
        .tokenProvider(TokenProviderType.parse(tokenProvider))
        .build();
  }
}
