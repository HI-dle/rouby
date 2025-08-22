package com.rouby.user.application.dto.info;

import com.rouby.user.domain.entity.NotificationType;
import com.rouby.user.domain.entity.User;
import java.util.Set;
import java.util.stream.Collectors;

public record UserInfo(
    Long id,
    String nickname,
    HealthStatusKeywordsInfo healthStatusKeywords,
    ProfileKeywordsInfo profileKeywords,
    CommunicationToneInfo communicationTone,
    Set<NotificationSettingInfo> notificationSettings
) {

  public static UserInfo of(User user) {
    return new UserInfo(
        user.getId(),
        user.getNickname(),
        new HealthStatusKeywordsInfo(user.getHealthStatusKeywords().getHealthStatusKeywords()),
        new ProfileKeywordsInfo(user.getProfileKeywords().getProfileKeywords()),
        new CommunicationToneInfo(user.getCommunicationToneValues()),
        user.getNotificationSettings().stream()
            .map(ns -> new NotificationSettingInfo(ns.getNotificationType(), ns.isEnabled()))
            .collect(Collectors.toSet())
    );
  }

  public record HealthStatusKeywordsInfo(Set<String> keywords) {

  }

  public record ProfileKeywordsInfo(Set<String> keywords) {

  }

  public record CommunicationToneInfo(Set<String> tones) {

  }

  public record NotificationSettingInfo(NotificationType notificationType, boolean isEnabled) {

  }

}
