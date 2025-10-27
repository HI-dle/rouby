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
  private static final String TYPE_FEEDBACK = "FEEDBACK";

  public static List<CreateNotificationEventCommand> from(
      FeedbackNotiTargetUserInfo targetUserInfo, String url) {

    if (targetUserInfo == null || !targetUserInfo.notificationEnabled()) {
      return Collections.emptyList();
    }

    return targetUserInfo.deviceInfos() == null
        ? Collections.emptyList()
        : targetUserInfo.deviceInfos().stream()
            .filter(d -> d != null && d.deviceToken() != null && !d.deviceToken().isBlank())
            .map(d ->
                CreateNotificationEventCommand.builder()
                    .userId(targetUserInfo.userId())
                    .nickname(targetUserInfo.nickname())
                    .tokenProviderType(d.tokenProvider())
                    .deviceToken(d.deviceToken())
                    .url(url)
                    .notificationType(TYPE_FEEDBACK)
                    .build())
        .toList();
  }
}
