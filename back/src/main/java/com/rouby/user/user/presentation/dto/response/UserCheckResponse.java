package com.rouby.user.user.presentation.dto.response;

import com.rouby.user.user.application.dto.info.UserDetailInfo;
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


  public static UserCheckResponse from(UserDetailInfo userDetailInfo) {
    return UserCheckResponse.builder()
        .id(userDetailInfo.id())
        .email(userDetailInfo.email())
        .nickname(userDetailInfo.nickname())
        .healthStatusKeywords(userDetailInfo.healthStatusKeywords())
        .profileKeywords(userDetailInfo.profileKeywords())
        .communicationTone(userDetailInfo.communicationTone())
        .onboardingStatePath(userDetailInfo.onboardingState().getRedirectPath())
        .build();
  }

}
