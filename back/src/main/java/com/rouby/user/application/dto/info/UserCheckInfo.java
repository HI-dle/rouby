package com.rouby.user.application.dto.info;

import com.rouby.user.domain.entity.OnboardingState;
import com.rouby.user.domain.entity.User;
import java.util.Set;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
@Builder
public record UserCheckInfo(Long id,
                            String email,
                            String nickname,
                            Set<String> healthStatusKeywords,
                            Set<String> profileKeywords,
                            Set<String> communicationTone,
                            OnboardingState onboardingState
) {
  public static UserCheckInfo from(User user) {
    return new UserCheckInfo(
        user.getId(),
        user.getEmail(),
        user.getNickname(),
        user.getHealthStatusKeywords().getHealthStatusKeywords(),
        user.getProfileKeywords().getProfileKeywords(),
        user.getCommunicationTone().getRoubyCommunicationTone(),
        user.getOnboardingState()
    );
  }
}
