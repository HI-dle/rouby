package com.rouby.user.device.application.dto.command;

import lombok.Builder;

@Builder
public record DeleteUserDeviceCommand(
    Long userId,
    String deviceToken,
    String tokenProvider
) {
}
