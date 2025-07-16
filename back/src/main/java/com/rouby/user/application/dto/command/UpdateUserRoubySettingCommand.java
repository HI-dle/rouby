package com.rouby.user.application.dto.command;

import com.rouby.user.domain.entity.CommunicationTone;
import com.rouby.user.domain.entity.NotificationSetting;
import com.rouby.user.domain.entity.NotificationType;
import com.rouby.user.domain.entity.User;
import java.util.Set;
import java.util.stream.Collectors;

public record UpdateUserRoubySettingCommand(
    Set<String> communicationTone,
    Set<NotificationSettingCommand> notificationSettings
) {
  public CommunicationTone toCommunicationTone() {
    return CommunicationTone.of(communicationTone);
  }

  public Set<NotificationSetting> toNotificationSettings(User user) {
    return notificationSettings.stream()
        .map(setting -> setting.toEntityWithUser(user))
        .collect(Collectors.toSet());
  }

  public record NotificationSettingCommand(
      NotificationType notificationType,
      boolean enabled
  ) {
    public NotificationSetting toEntityWithUser(User user) {
      return NotificationSetting.builder()
          .user(user)
          .notificationType(notificationType)
          .isEnabled(enabled)
          .build();
    }
  }
}

