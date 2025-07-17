package com.rouby.user.application.dto.info;

import com.rouby.user.domain.entity.CommunicationTone;
import com.rouby.user.domain.entity.CurrentStatusKeywords;
import com.rouby.user.domain.entity.HealthStatusKeywords;
import com.rouby.user.domain.entity.InterestKeywords;
import com.rouby.user.domain.entity.OnboardingState;
import com.rouby.user.domain.entity.User;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
@Builder
public record UserCheckInfo(Long id,
                            String email,
                            String nickname,
                            CurrentStatusKeywords currentStatusKeywords,
                            HealthStatusKeywords healthStatusKeywords,
                            InterestKeywords interestKeywords,
                            CommunicationTone communicationTone,
                            OnboardingState onboardingState
) {
  public static UserCheckInfo from(User user) {
    return new UserCheckInfo(
        user.getId(),
        user.getEmail(),
        user.getNickname(),
        user.getCurrentStatusKeywords(),
        user.getHealthStatusKeywords(),
        user.getInterestKeywords(),
        user.getCommunicationTone(),
        user.getOnboardingState()
    );
  }
}
