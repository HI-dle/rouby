package com.rouby.user.user.application.dto;

import com.rouby.user.device.application.dto.UserDeviceInfo;
import com.rouby.user.user.application.dto.info.UserDetailInfo;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserInfoWithDeviceInfos(
    String nickname,
    Set<String> healthStatusKeywords,
    Set<String> profileKeywords,
    Set<String> communicationTone,
    Boolean notificationEnabled,
    List<UserDeviceInfo> deviceInfos
) {

  public static UserInfoWithDeviceInfos from(UserDetailInfo userInfo, List<UserDeviceInfo> deviceInfos) {

    return UserInfoWithDeviceInfos.builder()
        .nickname(userInfo.nickname())
        .healthStatusKeywords(userInfo.healthStatusKeywords())
        .profileKeywords(userInfo.profileKeywords())
        .communicationTone(userInfo.communicationTone())
        .notificationEnabled(userInfo.notificationEnabled())
        .deviceInfos(deviceInfos)
        .build();
  }
}
