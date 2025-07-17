package com.rouby.user.domain.entity;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
public enum OnboardingState {


  ROUBY_SETTING_BEFORE("/onboarding/루비세팅첫경로"),

  USER_INFO_SETTING_BEFORE("/onboarding/nickname-setting"),

  COMPLETED("/기본경로생겼을때넣어야합니다.");

  private final String redirectPath;

  OnboardingState(String redirectPath) {
    this.redirectPath = redirectPath;
  }

  public String getRedirectPath() {
    return redirectPath;
  }

}
