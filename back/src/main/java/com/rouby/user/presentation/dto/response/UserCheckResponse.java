package com.rouby.user.presentation.dto.response;

import com.rouby.user.application.dto.info.UserCheckInfo;
import java.util.Set;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
@Builder
public record UserCheckResponse(Long id,
                                String email,
                                String nickname,
                                Set<String> healthStatusKeywords,
                                Set<String> profileKeywords,
                                Set<String> communicationTone,
                                String onboardingStatePath) {


  public static UserCheckResponse from(UserCheckInfo userCheckInfo) {
    return UserCheckResponse.builder()
        .id(userCheckInfo.id())
        .email(userCheckInfo.email())
        .nickname(userCheckInfo.nickname())
        .healthStatusKeywords(userCheckInfo.healthStatusKeywords())
        .profileKeywords(userCheckInfo.profileKeywords())
        .communicationTone(userCheckInfo.communicationTone())
        .onboardingStatePath(userCheckInfo.onboardingState().getRedirectPath())
        .build();
  }

}
