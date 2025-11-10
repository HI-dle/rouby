package com.rouby.user.user.application.dto.command;

public record RefreshTokenCommand(
    String accessToken,
    String refreshToken
) {

}
