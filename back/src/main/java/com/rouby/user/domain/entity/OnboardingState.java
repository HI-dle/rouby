package com.rouby.user.domain.entity;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 17.
 */
public enum OnboardingState {

  USER_INFO_SETTING_BEFORE("/onboarding/nickname-setting"),

  ROUBY_SETTING_BEFORE("/onboarding/speech-setting"),

  COMPLETED("/");

  private final String redirectPath;

  OnboardingState(String redirectPath) {
    this.redirectPath = redirectPath;
  }

  public String getRedirectPath() {
    return redirectPath;
  }

  OnboardingState next() {
    return switch (this) {
      case USER_INFO_SETTING_BEFORE -> ROUBY_SETTING_BEFORE;
      case ROUBY_SETTING_BEFORE -> COMPLETED;
      case COMPLETED -> null;
    };
  }

  boolean canTransitTo(OnboardingState nextState) {
    return this.next() == nextState;
  }
}
