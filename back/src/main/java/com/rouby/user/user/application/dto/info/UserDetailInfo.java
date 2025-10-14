package com.rouby.user.user.application.dto.info;

import com.rouby.user.user.domain.entity.OnboardingState;
import com.rouby.user.user.domain.entity.User;
import java.util.Set;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
@Builder
public record UserDetailInfo(
    Long id,
    String email,
    String nickname,
    Set<String> healthStatusKeywords,
    Set<String> profileKeywords,
    Set<String> communicationTone,
    OnboardingState onboardingState,
    boolean notificationEnabled
) {

  public static UserDetailInfo from(User user) {

    return UserDetailInfo.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .healthStatusKeywords(user.getHealthStatusKeywords().getHealthStatusKeywords())
        .profileKeywords(user.getProfileKeywords().getProfileKeywords())
        .communicationTone(user.getCommunicationToneValues())
        .onboardingState(user.getOnboardingState())
        .notificationEnabled(user.isNotificationEnabled())
        .build();
  }
}
