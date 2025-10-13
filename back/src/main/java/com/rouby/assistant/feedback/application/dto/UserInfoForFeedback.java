package com.rouby.assistant.feedback.application.dto;

import com.rouby.user.device.application.dto.UserDeviceInfo;
import com.rouby.user.user.application.dto.UserInfoWithDeviceInfos;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserInfoForFeedback(
    String nickname,
    Set<String> healthStatusKeywords,
    Set<String> profileKeywords,
    Set<String> communicationTone,
    Boolean notificationEnabled,
    List<DeviceInfoForFeedback> deviceInfos
) {

  public static UserInfoForFeedback from(UserInfoWithDeviceInfos userInfo) {

    return UserInfoForFeedback.builder()
        .nickname(userInfo.nickname())
        .healthStatusKeywords(userInfo.healthStatusKeywords())
        .profileKeywords(userInfo.profileKeywords())
        .communicationTone(userInfo.communicationTone())
        .notificationEnabled(userInfo.notificationEnabled())
        .deviceInfos(userInfo.deviceInfos().stream()
            .map(DeviceInfoForFeedback::from)
            .toList())
        .build();
  }

  @Builder
  public record DeviceInfoForFeedback(
      String deviceType,
      String deviceToken,
      String tokenProvider
  ) {

    public static DeviceInfoForFeedback from(UserDeviceInfo userDeviceInfo) {

      return DeviceInfoForFeedback.builder()
          .deviceType(userDeviceInfo.deviceType())
          .deviceToken(userDeviceInfo.deviceToken())
          .tokenProvider(userDeviceInfo.tokenProvider())
          .build();
    }
  }
}
