package com.rouby.user.device.presentation.dto.request;

import com.rouby.user.device.application.dto.command.DeleteUserDeviceCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record DeleteUserDeviceRequest(
    @NotBlank @Length(max=50) @Pattern(regexp = "(?i)^(FCM|APNs)$")
    String tokenProvider,
    @NotBlank @Length(max=500) String deviceToken
) {

  public DeleteUserDeviceCommand toCommand(Long userId) {

    return DeleteUserDeviceCommand.builder()
        .userId(userId)
        .tokenProvider(tokenProvider)
        .deviceToken(deviceToken)
        .build();
  }
}
