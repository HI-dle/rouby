package com.rouby.user.application.dto.info;

import com.rouby.user.domain.entity.NotificationSetting;
import com.rouby.user.domain.entity.NotificationType;
import com.rouby.user.domain.entity.User;
import java.util.Set;
import java.util.stream.Collectors;

public record RoubySettingInfo(
    Set<String> communicationTone,
    Set<NotificationSettingInfo> notificationSettings
) {

  public static RoubySettingInfo from(User user) {
    return new RoubySettingInfo(
        user.getCommunicationTone().getRoubyCommunicationTone(),
        user.getNotificationSettings().stream()
            .map(NotificationSettingInfo::from)
            .collect(Collectors.toSet())
    );
  }

  public record NotificationSettingInfo(
      NotificationType notificationType,
      boolean enabled
  ) {
    public static NotificationSettingInfo from(NotificationSetting setting) {
      return new NotificationSettingInfo(
          setting.getNotificationType(),
          setting.isEnabled());
    }
  }
}

