package com.rouby.user.presentation.dto.request;

import com.rouby.user.application.dto.command.UpdateUserRoubySettingCommand;
import com.rouby.user.application.dto.command.UpdateUserRoubySettingCommand.NotificationSettingCommand;
import com.rouby.user.domain.entity.NotificationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

public record UpdateRoubySettingRequest(
    Set<@NotEmpty(message = "말투가 없습니다.") String> communicationTone,
    @NotNull(message = "알림이 null입니다.")
    Set<NotificationSettingRequest> notificationSettings) {

  public UpdateUserRoubySettingCommand toCommand() {
    return new UpdateUserRoubySettingCommand(communicationTone,
        this.notificationSettings.stream()
        .map(n ->
            new NotificationSettingCommand(n.notificationType, n.enabled))
        .collect(Collectors.toSet()));
  }

  public record NotificationSettingRequest(
      NotificationType notificationType,
      boolean enabled) {

  }
}
