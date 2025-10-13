package com.rouby.notification.notificationEvent.application.dto;

import com.rouby.assistant.feedback.application.dto.FeedbackNotiTargetUserInfo;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateNotificationEventCommand(
    Long userId,
    String nickname,
    String tokenProviderType,
    String deviceToken,
    String url,
    String notificationType
) {

  public static List<CreateNotificationEventCommand> from(
      FeedbackNotiTargetUserInfo targetUserInfo, String url) {

    return targetUserInfo.deviceInfos() == null
        ? Collections.emptyList()
        : targetUserInfo.deviceInfos().stream()
        .map(d ->
            CreateNotificationEventCommand.builder()
                .userId(targetUserInfo.userId())
                .nickname(targetUserInfo.nickname())
                .tokenProviderType(d.tokenProvider())
                .deviceToken(d.deviceToken())
                .url(url)
                .notificationType("FEEDBACK")
                .build())
        .toList();
  }
}
