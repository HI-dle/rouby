package com.rouby.user.device.fixture;

import com.rouby.user.device.presentation.dto.request.DeleteUserDeviceRequest;
import com.rouby.user.device.presentation.dto.request.RegisterUserDeviceRequest;

public class UserDeviceFixture {

  public static RegisterUserDeviceRequest getSuccessRequest() {
    return RegisterUserDeviceRequest.builder()
        .deviceToken("1234")
        .tokenProvider("FCM")
        .appType("WEB")
        .appVersion("0.0.1")
        .deviceType("DESKTOP")
        .os("Windows")
        .browser("Safari")
        .userAgent("test")
        .build();
  }

  public static RegisterUserDeviceRequest getInvalidDeviceTypeRequest() {
    return RegisterUserDeviceRequest.builder()
        .deviceToken("1234")
        .tokenProvider("FCM")
        .appType("WEB")
        .appVersion("0.0.1")
        .deviceType("BRIDGE")
        .os("Windows")
        .browser("Safari")
        .userAgent("test")
        .build();
  }

  public static DeleteUserDeviceRequest getInvalidDeviceTypeDeleteRequest() {
    return DeleteUserDeviceRequest.builder()
        .deviceToken("1234")
        .tokenProvider("dkfjg")
        .build();
  }

  public static DeleteUserDeviceRequest getSuccessDeleteRequest() {
    return DeleteUserDeviceRequest.builder()
        .deviceToken("1234")
        .tokenProvider("FCM")
        .build();
  }
}
