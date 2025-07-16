package com.rouby.user.presentation.dto.response;

import com.rouby.user.application.dto.info.UserCheckInfo;
import com.rouby.user.domain.entity.CommunicationTone;
import com.rouby.user.domain.entity.CurrentStatusKeywords;
import com.rouby.user.domain.entity.HealthStatusKeywords;
import com.rouby.user.domain.entity.InterestKeywords;
import com.rouby.user.domain.entity.OnboardingState;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
@Builder
public record UserCheckResponse(Long id,
                                String email,
                                String nickname,
                                CurrentStatusKeywords currentStatusKeywords,
                                HealthStatusKeywords healthStatusKeywords,
                                InterestKeywords interestKeywords,
                                CommunicationTone communicationTone,
                                String onboardingStatePath) {


  public static UserCheckResponse from(UserCheckInfo userCheckInfo) {
    return UserCheckResponse.builder()
        .id(userCheckInfo.id())
        .email(userCheckInfo.email())
        .nickname(userCheckInfo.nickname())
        .currentStatusKeywords(userCheckInfo.currentStatusKeywords())
        .healthStatusKeywords(userCheckInfo.healthStatusKeywords())
        .interestKeywords(userCheckInfo.interestKeywords())
        .communicationTone(userCheckInfo.communicationTone())
        .onboardingStatePath(userCheckInfo.onboardingState().getRedirectPath())
        .build();
  }

}
