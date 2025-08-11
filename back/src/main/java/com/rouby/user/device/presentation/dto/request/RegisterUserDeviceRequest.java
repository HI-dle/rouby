package com.rouby.user.device.presentation.dto.request;

import com.rouby.user.device.application.dto.command.RegisterUserDeviceCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record RegisterUserDeviceRequest(
    @NotBlank @Length(max=50) @Pattern(regexp = "(?i)^(FCM|APNs)$")
    String tokenProvider,
    @NotBlank @Length(max=500) String deviceToken,
    @NotBlank @Length(max=50)
    @Length(max=50) @Pattern(regexp = "(?i)^(ANDROID|IOS|WEB)$")
    String appType,
    @NotBlank @Length(max=50) String appVersion,
    @NotBlank @Length(max=50) @Pattern(regexp = "(?i)^(DESKTOP|MOBILE|TABLET|TV|EMBEDDED)$")
    String deviceType,
    @Length(max=255) String os,
    @Length(max=255) String browser,
    @NotBlank @Length(max=255) String userAgent
) {

  public RegisterUserDeviceCommand toCommand(Long userId) {

    return RegisterUserDeviceCommand.builder()
        .userId(userId)
        .tokenProvider(tokenProvider)
        .deviceToken(deviceToken)
        .appType(appType)
        .appVersion(appVersion)
        .deviceType(deviceType)
        .os(os)
        .browser(browser)
        .userAgent(userAgent)
        .build();
  }
}
