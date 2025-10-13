package com.rouby.assistant.feedback.application.dto;

import com.rouby.assistant.feedback.application.dto.UserInfoForFeedback.DeviceInfoForFeedback;
import java.util.List;
import lombok.Builder;

@Builder
public record FeedbackNotiTargetUserInfo(
    Long userId,
    String nickname,
    Boolean notificationEnabled,
    List<DeviceInfoForFeedback> deviceInfos
) {

  public static FeedbackNotiTargetUserInfo from(Long userId, UserInfoForFeedback userInfo) {

    return FeedbackNotiTargetUserInfo.builder()
        .userId(userId)
        .nickname(userInfo.nickname())
        .notificationEnabled(userInfo.notificationEnabled())
        .deviceInfos(userInfo.deviceInfos())
        .build();
  }
}
