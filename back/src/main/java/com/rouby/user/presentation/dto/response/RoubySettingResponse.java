package com.rouby.user.presentation.dto.response;

import com.rouby.user.application.dto.info.RoubySettingInfo;
import com.rouby.user.domain.entity.NotificationType;
import java.util.Set;
import java.util.stream.Collectors;

public record RoubySettingResponse(
    Set<String> communicationTone,
    Set<NotificationSettingResponse> notificationSettings
) {

  public record NotificationSettingResponse(
      NotificationType notificationType,
      boolean enabled
  ) {

  }

  public static RoubySettingResponse from(RoubySettingInfo roubySettingInfo) {

    return new RoubySettingResponse(roubySettingInfo.communicationTone(),
        roubySettingInfo.notificationSettings().stream()
            .map(s -> new NotificationSettingResponse(s.notificationType(), s.enabled()))
            .collect(Collectors.toSet()));
  }
}
